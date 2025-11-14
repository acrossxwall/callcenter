package cc.efit.dispatch.biz.service.impl;

import cc.efit.call.api.constants.DispatchKeyConstants;
import cc.efit.call.api.domain.CallCustomer;
import cc.efit.call.api.domain.CallSystemStatistics;
import cc.efit.call.api.domain.CallTask;
import cc.efit.call.api.domain.CallTaskStatistics;
import cc.efit.call.api.enums.CallCustomerStatusEnum;
import cc.efit.call.api.enums.CallTaskEnum;
import cc.efit.call.api.repository.*;
import cc.efit.call.api.vo.line.CallLineInfo;
import cc.efit.call.api.vo.line.DispatchLineVo;
import cc.efit.call.api.vo.record.CallRecordStatsDTO;
import cc.efit.call.api.vo.task.CallTaskConcurrency;
import cc.efit.call.api.vo.task.SystemStatisticsInfo;
import cc.efit.call.api.vo.task.SystemTaskInfo;
import cc.efit.dispatch.api.constant.DispatchRedisKeyConstant;
import cc.efit.utils.DateUtil;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dial.api.req.DialPhoneReq;
import cc.efit.dial.api.req.LineInfoReq;
import cc.efit.dialogue.api.vo.global.TemplateGlobalInterruptInfo;
import cc.efit.dialogue.api.vo.global.TemplateGlobalNoReplyInfo;
import cc.efit.dispatch.api.enums.DispatchCallTaskTypeEnum;
import cc.efit.dispatch.biz.call.CallTaskHandler;
import cc.efit.dispatch.biz.call.CallTaskHandlerFactory;
import cc.efit.dispatch.biz.config.CallConfig;
import cc.efit.dispatch.biz.config.DialDataExecutor;
import cc.efit.dispatch.biz.intelligent.IntelligentAdjustConcurrent;
import cc.efit.dispatch.biz.service.CallTaskService;
import cc.efit.dispatch.biz.service.TaskCommonService;
import cc.efit.dispatch.biz.thread.DialCallCustomerThread;
import cc.efit.dispatch.biz.thread.LoadWaitCallCustomerThread;
import cc.efit.json.utils.JsonUtils;
import cc.efit.process.api.res.TemplateGlobalSettingRes;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.redis.utils.RedisLock;
import cc.efit.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.*;
import static cc.efit.process.api.constants.DialogueRedisConstant.TEMPLATE_GLOBAL_INTERRUPT;
import static cc.efit.process.api.constants.DialogueRedisConstant.TEMPLATE_GLOBAL_NO_REPLY;

@Service
@Slf4j
public class CallTaskServiceImpl implements CallTaskService {
    @Autowired
    private   CallTaskRepository callTaskRepository;
    @Autowired
    private   RedisUtils redisUtils;
    @Autowired
    private   RedisLock redisLock;
    @Autowired
    private   TaskCommonService commonService;
    @DialDataExecutor
    private ExecutorService dialExecutor;
    @Autowired
    private CallCustomerRepository customerRepository;
    @Autowired
    private CallConfig callConfig;
    @Autowired
    private CallTaskHandlerFactory callTaskHandlerFactory;
    @Autowired
    private IntelligentAdjustConcurrent intelligentAdjustConcurrent;
    @Autowired
    private CallRecordRepository callRecordRepository;
    @Autowired
    private CallTaskStatisticsRepository callTaskStatisticsRepository;
    @Autowired
    private CallSystemStatisticsRepository callSystemStatisticsRepository;
    @Override
    public CallTask findCallTaskFromDb(Integer id) {
        return callTaskRepository.findById(id).orElse(null);
    }

    @Override
    public CallTask findCallTaskFromRedis(Integer id) {
        String key = DISPATCH_TASK_INFO_KEY.formatted(id);
        return redisUtils.get(key, CallTask.class);
    }

    @Override
    public CallTask findCallTaskFromDbOrRedis(Integer id) {
        CallTask task = findCallTaskFromRedis(id);
        if (task==null) {
            task = findCallTaskFromDb(id);
            saveCallTaskToRedis(task);
        }
        return task;
    }

    @Override
    public void saveCallTaskToRedis(CallTask callTask) {
        if (callTask==null) {
            return;
        }
        String key = DISPATCH_TASK_INFO_KEY.formatted(callTask.getId());
        redisUtils.set(key, JsonUtils.toJsonString(callTask), 60 * 60 * 24 );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispatchCallTask(Integer taskId) {
        //先判断是否已经在拨打任务
        boolean alreadyStart = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_CALL_START_KEY, taskId);
        if (alreadyStart) {
            log.info("任务:{}已经再拨打中，无需重复拨打",taskId);
            return;
        }
        String taskLockKey = DISPATCH_TASK_LOCK_KEY.formatted(taskId);
        boolean taskLock = false ;
        try {
            taskLock = redisLock.tryLockResetExpire(taskLockKey, 10,  TimeUnit.SECONDS);
            if (!taskLock) {
                log.info("任务:{}正在处理中，加锁失败，无需重复拨打",taskId);
                return;
            }
            //再次校验是否已经在拨打任务
            alreadyStart = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_CALL_START_KEY, taskId);
            if (alreadyStart) {
                log.info("任务:{}在此判断拨打中，无需重复拨打",taskId);
                return;
            }
            CallTask callTask = findCallTaskFromDb(taskId);
            if (callTask==null) {
                log.info("任务:{}不存在，无需拨打",taskId);
                return;
            }
            if (CallTaskEnum.TaskCallStatus.PAUSE.getStatus().equals(callTask.getCallStatus())
                    || CallTaskEnum.TaskCallStatus.RUNNING.getStatus().equals(callTask.getCallStatus())) {
                log.info("任务:{}已经状态不正确:{}，无法拨打",taskId,callTask.getCallStatus());
                return;
            }
            //开始拨打
            //构建线路信息到缓存 ,并发统一调度
            initCallTaskResource(callTask);
            //清空上一次缓存的部分值
            clearOldLineSemaphore(taskId, callTask .getLineId());
            callTask.setCallStatus(CallTaskEnum.TaskCallStatus.RUNNING.getStatus());
            callTaskRepository.save(callTask);
            saveCallTaskToRedis(callTask);
            //启动加载待拨打号码的线程
            dialExecutor.execute(new LoadWaitCallCustomerThread(taskId));
            //启用一次智能调度
            intelligentAdjustConcurrent.adjustConcurrent(callTask.getLineId(),callTask.getDeptId());
            //添加redis set
            redisUtils.sSet(DispatchKeyConstants.DISPATCH_CALL_START_KEY, taskId);
        }finally {
            if (taskLock) {
                redisLock.unlock(taskLockKey);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseDispatchCallTask(Integer taskId) {
        //先判断是否已经正在暂停
        boolean alreadyPause = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_CALL_PAUSE_KEY, taskId);
        if (alreadyPause) {
            log.info("任务:{}正在暂停中，无需重复暂停",taskId);
            return;
        }
        String taskPauseLockKey = DISPATCH_TASK_PAUSE_LOCK_KEY.formatted(taskId);
        boolean taskLock = false ;
        try {
            taskLock = redisLock.tryLockResetExpire(taskPauseLockKey, 10, TimeUnit.SECONDS);
            if (!taskLock) {
                log.info("任务:{}正在暂停中，加锁失败，无需重复暂停", taskId);
                return;
            }
            alreadyPause = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_CALL_PAUSE_KEY, taskId);
            if (alreadyPause) {
                log.info("任务:{}已暂停，无需重复暂停",taskId);
                return;
            }
            //移除拨打任务队列
            redisUtils.setRemove(DispatchKeyConstants.DISPATCH_CALL_START_KEY, taskId);
            cancelLoadCustomerData(taskId);
            //如果有重呼，此处还需要重呼数据
            callTaskRepository.updateCallStatusById(taskId,CallTaskEnum.TaskCallStatus.PAUSE.getStatus());
            redisUtils.sSet(DispatchKeyConstants.DISPATCH_CALL_PAUSE_KEY, taskId);
        }finally {
            if (taskLock) {
                redisLock.unlock(taskPauseLockKey);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean loadWaitCallCustomer(Integer taskId,boolean start) {
        log.info("开始加载待呼叫的数据:{}",taskId);
        CallTask task = findCallTaskFromDb(taskId);
        redisUtils.sSet(DispatchKeyConstants.DISPATCH_REDIS_LOAD_DATA, taskId);
        int concurrent = task.getLineConcurrent();
        if (!start) {
            //不是启动的时候加载 判断并发是否跑满
            checkDispatchTaskConcurrency(taskId);
        }
        String waitCallCustomerKey = DISPATCH_TASK_FIRST_CUSTOMER.formatted(taskId);
        long size = redisUtils.lGetListSize(waitCallCustomerKey);
        if (size <= concurrent) {
            log.info("taskId:{},待呼叫客户数量小于并发数,开始加载待呼叫客户", taskId);
            String customerLockKey = DISPATCH_TASK_CUSTOMER_LOCK.formatted(taskId);
            boolean customerLock = false;
            try {
                customerLock = redisLock.tryLockResetExpire(customerLockKey, 10, TimeUnit.SECONDS);
                if (!customerLock) {
                    log.info("taskId:{},加载待呼叫客户失败,锁被占用", taskId);
                    return false;
                }
                //开始加载数据库待拨打数据
                int loadSize = callConfig.getFactor() * concurrent;
                List<CallCustomer> customers = customerRepository.findByTaskIdAndStatus(taskId, CallCustomerStatusEnum.CustomerStatus.NOT_CALL.getStatus(),
                                        Limit.of(loadSize));
                if (CollectionUtils.isEmpty(customers)) {
                    log.info("taskId:{},待呼叫客户数量为0", taskId);
                    return true;
                }
                //批量更新状态
                buildWaitCallCustomer(customers, task);
                //最后启用调度线程去拨打
                if(start){
                    //第一次时启动，后续就不需要了
                    dialExecutor.execute(new DialCallCustomerThread(taskId));
                }
            }finally {
                if (customerLock) {
                    redisLock.unlock(customerLockKey);
                }
            }
        }
        return false;
    }

    @Override
    public void dispatchCallCustomer(Integer taskId) {
        log.info("开始调度任务：{}", taskId);
        boolean exists = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_CALL_START_KEY, taskId);
        if (!exists) {
            log.info("任务不存在拨打：{}", taskId);
            return;
        }
        RSemaphore semaphore = commonService.getTaskDispatchConcurrencySemaphore(taskId);
        int availablePermits = semaphore.availablePermits();
        if (availablePermits<=0) {
            log.info("任务调度并发数已达到上限：{}", taskId);
            return;
        }
        log.info("任务taskId:{},可调度并发数：{}",taskId, availablePermits);
        CallTaskHandler taskHandler = callTaskHandlerFactory.getHandler(DispatchCallTaskTypeEnum.FIRST_CALL.ordinal());
        taskHandler.handleCallTask(findCallTaskFromRedis(taskId),semaphore,availablePermits);
    }

    @Override
    public void releaseTaskDispatchConcurrencySemaphore(Integer taskId) {
        RSemaphore semaphore = commonService.getTaskDispatchConcurrencySemaphore(taskId);
        semaphore.release();
        //异步调用拨打
        dialExecutor.execute(new DialCallCustomerThread(taskId));
    }

    @Override
    public void dispatchCallTaskLoadData(Integer taskId) {
        //需要加锁，确保只有一个线程启动
        String taskLoadLockKey = DISPATCH_TASK_LOAD_DATA_LOCK_KEY.formatted(taskId);
        boolean taskLoadDataLock = false ;
        try {
            taskLoadDataLock = redisLock.tryLockResetExpire(taskLoadLockKey, 10, TimeUnit.SECONDS);
            if (!taskLoadDataLock) {
                log.info("任务:{}加载数据正在处理中，加锁失败，无需重复加载", taskId);
                return;
            }
            //再次判断是否已经存在加载线程
            boolean alreadyLoad = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_REDIS_LOAD_DATA, taskId);
            if (alreadyLoad) {
                log.info("任务:{}加载数据线程已存在，无需重复加载", taskId);
                return;
            }
            dialExecutor.execute(new LoadWaitCallCustomerThread(taskId));
            redisUtils.sSet(DispatchKeyConstants.DISPATCH_REDIS_LOAD_DATA, taskId);
        } catch (Exception e) {
            log.error("任务加载数据线程失败",   e);
        } finally {
            if (taskLoadDataLock) {
                redisLock.unlock(taskLoadLockKey);
            }
        }
    }

    @Override
    public List<DispatchLineVo> findRunningDispatchLineVoList() {
        return callTaskRepository.selectRunningDispatchLineVo();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statistics(String params) {
        Map<String,Object> map = JsonUtils.parseObjectToMap(params);
        LocalDate startTime = LocalDate.now();
        LocalDate endTime = startTime.plusDays(1);
        if (map.get("startTime") != null) {
            startTime = DateUtil.parseLocalDate ((String) map.get("startTime"));
        }
        if (map.get("endTime") != null) {
            endTime = DateUtil.parseLocalDate ((String) map.get("endTime"));
        }
        List<CallRecordStatsDTO> list = callRecordRepository.findCallStatsByDateRange(startTime,endTime);
        if (list==null || list.isEmpty()) {
            log.info("没有统计数据");
            return;
        }
        list.forEach(this::buildTaskStatistics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkPauseTaskStatus() {
        Set<Object> set = redisUtils.sGet(DispatchKeyConstants.DISPATCH_CALL_PAUSE_KEY);
        if (set==null || set.isEmpty()) {
            log.info("没有暂停中的任务，do nothing");
            return;
        }
        set.forEach(this::buildTaskPauseStatus);
    }

    @Override
    public void checkCallTaskStatus() {
        log.info("检测执行中的任务拨打状态是否完成");
        Set<Object> set = redisUtils.sGet(DispatchKeyConstants.DISPATCH_CALL_START_KEY);
        if (set==null || set.isEmpty()) {
            log.info("没有执行中的任务，do nothing");
            return;
        }
        set.forEach(this::buildTaskStatus);
    }

    @Override
    public void systemStatistics(String params) {
        LocalDate startTime = LocalDate.now();
        LocalDate endTime = startTime.plusDays(1);
        List<SystemStatisticsInfo> list = callTaskStatisticsRepository.selectSystemStatisticsInfo(startTime,endTime);
        if (list==null || list.isEmpty()) {
            log.info("没有数据，do nothing");
            return;
        }
        //查询运行中的任务并发
        List<SystemTaskInfo> runningTask = callTaskRepository.selectRunningSystemTask();
        Map<String,Integer> runningConcurrent = new HashMap<>();
        if (runningTask!=null && !runningTask.isEmpty()) {
            //所有的任务
            Map<String,List<Integer>> map = runningTask.stream().collect(Collectors.groupingBy(s-> s.orgId()+"_" +s.deptId() +"_"+s.userId(),
                    Collectors.mapping(SystemTaskInfo::taskId,Collectors.toList())));
            map.forEach((k,v)->{
                runningConcurrent.put(k,v.stream().mapToInt(this::buildTaskConcurrent).sum());
            });

        }
        list.forEach(s->buildSystemStatisticsInfo(s,runningConcurrent));
    }

    private int buildTaskConcurrent(Integer taskId) {
        return intelligentAdjustConcurrent.findTaskAdjustConcurrent(taskId);
    }

    private void buildSystemStatisticsInfo(SystemStatisticsInfo systemStatisticsInfo,Map<String,Integer> runningConcurrent) {
        log.info("统计系统信息，systemStatisticsInfo:{}",systemStatisticsInfo);
        CallSystemStatistics systemStatistics = new CallSystemStatistics();
        systemStatistics.setCallDate(LocalDate.now());
        systemStatistics.setCallTime(DateUtil.getTodayHours());
        systemStatistics.setTotalCustomers(systemStatisticsInfo.importedData().intValue());
        systemStatistics.setCalledCustomers(systemStatisticsInfo.calledData().intValue());
        systemStatistics.setConnectCount(systemStatisticsInfo.connectedData().intValue());
        systemStatistics.setDuration(systemStatisticsInfo.duration().intValue());
        systemStatistics.setOrgId(systemStatisticsInfo.orgId());
        systemStatistics.setDeptId(systemStatisticsInfo.deptId());
        systemStatistics.setUserId(systemStatisticsInfo.userId());
        systemStatistics.setConcurrent(buildCurrentConcurrent(systemStatisticsInfo,runningConcurrent));
        callSystemStatisticsRepository.save(systemStatistics);
    }

    private Integer buildCurrentConcurrent(SystemStatisticsInfo systemStatisticsInfo,Map<String,Integer> runningConcurrent) {
        Integer orgId = systemStatisticsInfo.orgId();
        Integer deptId = systemStatisticsInfo.deptId();
        Integer userId = systemStatisticsInfo.userId();
        log.info("统计当前并发数，orgId:{},deptId:{},userId:{}",orgId,deptId,userId);
        return runningConcurrent.getOrDefault(orgId+"_"+deptId+"_"+userId,0);
    }

    private void buildTaskStatus(Object obj) {
        if (obj==null) {
            return;
        }
        log.info("检查执行的任务，taskId:{}",obj);
        Integer taskId = Integer.valueOf(obj.toString());
        boolean finish = checkCallTaskFinish(taskId);
        log.info("任务是否完成，taskId:{},finish:{}",taskId,finish);
        if (finish) {
            releaseTaskRedisKey(taskId);
            callTaskRepository.updateCallStatusById(taskId, CallTaskEnum.TaskCallStatus.FINISHED.getStatus());
            intelligentAdjustConcurrent.releaseTaskFinishConcurrent(taskId);
        }
    }

    private boolean checkCallTaskFinish(Integer taskId) {
        String waitCallCustomerKey = DISPATCH_TASK_FIRST_CUSTOMER.formatted(taskId);
        String recallKey = DISPATCH_TASK_REPEAT_CUSTOMER.formatted(taskId);
        return checkCallTaskWaitCall(waitCallCustomerKey)
                && checkCallTaskWaitCall(recallKey)
                && checkCallCustomerLeft(taskId, CallCustomerStatusEnum.CustomerStatus.NOT_CALL)
                && checkCallCustomerLeft(taskId, CallCustomerStatusEnum.CustomerStatus.CALLING);
    }

    private boolean checkCallTaskWaitCall(String customerKey) {
        long waitCall = redisUtils.lGetListSize(customerKey);
        return waitCall == 0 ;
    }

    private boolean checkCallCustomerLeft(Integer taskId, CallCustomerStatusEnum.CustomerStatus customerStatus) {
        long l = customerRepository.countByTaskIdAndStatus(taskId, customerStatus.getStatus());
        log.info("任务id:{},客户状态:{},剩余客户数量:{}",taskId,customerStatus ,l);
        return l == 0;
    }

    private void releaseTaskRedisKey(Integer taskId) {
        redisUtils.del(DISPATCH_TASK_FIRST_CUSTOMER.formatted(taskId),
                DISPATCH_TASK_REPEAT_CUSTOMER.formatted(taskId),
                DISPATCH_TASK_CONCURRENCY.formatted(taskId),
                DISPATCH_TASK_INFO_KEY.formatted(taskId),
                DISPATCH_TASK_ALREADY_CALL.formatted(taskId)
                );
        redisUtils.setRemove(DispatchKeyConstants.DISPATCH_CALL_START_KEY, taskId);
    }

    private void buildTaskPauseStatus(Object obj) {
        if (obj==null) {
            return;
        }
        log.info("检查暂停中的任务，taskId:{}",obj);
        Integer taskId = Integer.valueOf(obj.toString());
        if(intelligentAdjustConcurrent.releaseLinePauseConcurrent(taskId)) {
            redisUtils.setRemove(DispatchKeyConstants.DISPATCH_CALL_PAUSE_KEY,taskId);
            callTaskRepository.updateCallStatusById(taskId, CallTaskEnum.TaskCallStatus.PAUSED.getStatus());
        }
    }

    private void buildTaskStatistics(CallRecordStatsDTO statsDTO) {
        //task 仅更新基础字段，详细统计报表信息在统计表中
        callTaskRepository.updateCallTaskStatisticsInfo(statsDTO.totalCalls().intValue(),statsDTO.answeredCalls().intValue(),statsDTO.taskId());
        callTaskRepository.findById(statsDTO.taskId()).ifPresent(task -> {
            CallTaskStatistics statistics = callTaskStatisticsRepository.findByTaskIdAndCallDate(task.getId(),LocalDate.now());
            if (statistics == null) {
                statistics = new CallTaskStatistics();
                statistics.setTaskId(task.getId());
                statistics.setCallDate(LocalDate.now());
                //非web 项目 基础用户信息直接使用task
                statistics.setDeptId(task.getDeptId());
                statistics.setUserId(task.getUserId());
                statistics.setOrgId(task.getOrgId());
            }
            statistics.setTotalCustomers(task.getTodayCustomers() );
            statistics.setCalledCustomers(statsDTO.totalCalls().intValue());
            statistics.setConnectCount(statsDTO.answeredCalls().intValue());
            statistics.setDuration(statsDTO.totalDuration().intValue());
            callTaskStatisticsRepository.save(statistics);
        });
    }

    private void buildWaitCallCustomer(List<CallCustomer> customers, CallTask task) {
        List<Integer> ids = new ArrayList<>();
        Integer lineId = task.getLineId();
        LineInfoReq lineInfo = buildCallLineInfo(lineId);
        Integer callTemplateId = task.getCallTemplateId();
        TemplateGlobalSettingRes templateGlobalSettingRes = buildTemplateGlobalSettingRes(callTemplateId);
        String waitCallCustomerKey = DISPATCH_TASK_FIRST_CUSTOMER.formatted(task.getId());
        for (CallCustomer customer : customers) {
            DialPhoneReq req = new DialPhoneReq(lineInfo, buildCallCustomerReq(customer, task), templateGlobalSettingRes );
            redisUtils.lSet(waitCallCustomerKey, JsonUtils.toJsonString(req));
            ids.add(customer.getId());
            if (ids.size()== callConfig.getBatchSize()) {
                customerRepository.updateStatusByIds(ids, CallCustomerStatusEnum.CustomerStatus.LOADING.getStatus());
                ids.clear();
            }
        }
        if (!ids.isEmpty()) {
            customerRepository.updateStatusByIds(ids, CallCustomerStatusEnum.CustomerStatus.LOADING.getStatus());
        }
    }

    private CallCustomerInfoReq buildCallCustomerReq(CallCustomer customer, CallTask task) {
        return new CallCustomerInfoReq(customer.getId(),customer.getPhone(),task.getId(),task.getTaskName(),
                customer.getCallId(), customer.getCustomerInfo(),task.getCallTemplateId(),task.getTemplateName());
    }


    private TemplateGlobalSettingRes buildTemplateGlobalSettingRes(Integer callTemplateId) {
        String noReplyKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_NO_REPLY, callTemplateId);
        TemplateGlobalNoReplyInfo info = redisUtils.get(noReplyKey, TemplateGlobalNoReplyInfo.class);
        boolean enableNoReply = info != null && info.enabled();
        Integer noReplySeconds = info != null ? info.maxNoreplySeconds() : 0;
        String interruptKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_INTERRUPT, callTemplateId);
        TemplateGlobalInterruptInfo interruptInfo = redisUtils.get(interruptKey, TemplateGlobalInterruptInfo.class);
        boolean enableInterrupt = interruptInfo != null && interruptInfo.enabled();
        Integer interruptSeconds = interruptInfo != null ? interruptInfo.seconds() : 0;
        return new TemplateGlobalSettingRes(enableInterrupt, interruptSeconds, enableNoReply, noReplySeconds);
    }

    private void cancelLoadCustomerData(Integer taskId) {
        String customerLockKey = DISPATCH_TASK_CUSTOMER_LOCK.formatted(taskId);
        try {
            //需要阻塞，确保加载数据线程加载好了，此处不能释放调锁
            redisLock.lock(customerLockKey);
            String waitCallCustomerKey = DISPATCH_TASK_FIRST_CUSTOMER.formatted(taskId);
            redisUtils.del(waitCallCustomerKey);
            customerRepository.updateStatusByStatusAndTaskId(taskId, CallCustomerStatusEnum.CustomerStatus.LOADING.getStatus(),
                    CallCustomerStatusEnum.CustomerStatus.NOT_CALL.getStatus());
        }finally {
            redisLock.unlock(customerLockKey);
        }
    }

    private LineInfoReq buildCallLineInfo(Integer lineId) {
        CallLineInfo lineInfo = commonService.getTaskCallLineInfo(lineId);
        return new LineInfoReq(lineInfo.lineId(), lineInfo.lineName(),
                lineInfo.callNumber(),lineInfo.callPrefix(),false,lineInfo.gatewayName(),
                lineInfo.realm(),lineInfo.port());
    }

    private void checkDispatchTaskConcurrency(Integer taskId) {
        RSemaphore semaphore = commonService.getTaskDispatchConcurrencySemaphore(taskId);
        int availablePermits = semaphore.availablePermits();
        log.info("加载数据的线程判断任务并发 taskId:{},  availablePermits:{}", taskId, availablePermits);
        //启动调度线程
        if (availablePermits > 0) {
            dialExecutor.execute(new DialCallCustomerThread(taskId));
        }
    }

    private void clearOldLineSemaphore(Integer taskId, Integer lineId) {
        String taskAssignInfo = DISPATCH_TASK_ASSIGN_INFO_KEY.formatted(taskId);
        CallTaskConcurrency lastTask = redisUtils.get(taskAssignInfo, CallTaskConcurrency.class);
        if (lastTask==null || lastTask.getLineId().equals(lineId)) {
            log.info("任务:{}没有上一次拨打记录或者线路一样，无需清除",taskId);
            return;
        }
        RSemaphore semaphore = commonService.getTaskDispatchConcurrencySemaphore(taskId);
        if (semaphore.isExists()) {
            log.info("任务:{}上一次拨打线路:{}，清除上一次的信号量",taskId,lastTask.getLineId());
            semaphore.addPermits(-lastTask.getConcurrency());
        }
    }


    private void initCallTaskResource(CallTask task) {
        initLineInfo(task.getLineId());
        //后续其他的资源加载
    }

    private void initLineInfo(Integer lineId) {
        CallLineInfo lineInfo = commonService.getTaskCallLineInfo(lineId);
        if (lineInfo==null) {
            commonService.buildLineInfoToRedis(lineId);
        }
    }
}
