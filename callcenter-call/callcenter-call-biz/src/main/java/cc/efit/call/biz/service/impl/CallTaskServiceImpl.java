package cc.efit.call.biz.service.impl;

import cc.efit.call.api.constants.DispatchKeyConstants;
import cc.efit.call.api.domain.LineAssign;
import cc.efit.call.api.enums.CallCustomerStatusEnum;
import cc.efit.call.api.enums.CallTaskJobStatusEnum;
import cc.efit.call.api.repository.CallTaskStatisticsRepository;
import cc.efit.call.api.repository.LineAssignRepository;
import cc.efit.call.api.vo.task.CallStatusCountInfo;
import cc.efit.call.api.vo.task.TaskStatisticsInfo;
import cc.efit.call.api.vo.task.TaskSummaryInfo;
import cc.efit.call.biz.domain.CallTaskJob;
import cc.efit.call.biz.repository.CallTaskJobRepository;
import cc.efit.call.biz.service.CallCustomerService;
import cc.efit.call.biz.valid.CallTimeConfig;
import cc.efit.call.biz.valid.CallValidationService;
import cc.efit.call.biz.valid.TimeRange;
import cc.efit.dialogue.api.service.DialogueTemplateApi;
import cc.efit.dialogue.api.vo.template.TemplateInfo;
import cc.efit.call.api.enums.CallTaskEnum;
import cc.efit.call.api.vo.task.CallTaskInfo;
import cc.efit.call.api.domain.CallTask;
import cc.efit.exception.BadRequestException;
import cc.efit.job.IJobService;
import cc.efit.job.config.XxlJobProperties;
import cc.efit.job.core.BasicXxlJob;
import cc.efit.job.core.CronExpressionInfo;
import cc.efit.job.utils.CronExpressionUtils;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.call.biz.service.mapstruct.CallTaskMapper;
import cc.efit.call.api.repository.CallTaskRepository;
import cc.efit.call.biz.service.dto.CallTaskDto;
import cc.efit.call.biz.service.dto.CallTaskQueryCriteria;
import cc.efit.call.biz.service.CallTaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 呼叫任务表Service业务层处理
 * 
 * @author across
 * @date 2025-08-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallTaskServiceImpl implements CallTaskService {

    private final CallTaskRepository callTaskRepository;
    private final CallTaskMapper callTaskMapper;
    private final DialogueTemplateApi dialogueTemplateApi;
    private final CallCustomerService callCustomerService;
    private final RedisUtils redisUtils;
    private final RabbitTemplate rabbitTemplate;
    private final CallValidationService callValidationService;
    private final IJobService jobService;
    private final XxlJobProperties xxlJobProperties;
    private final CallTaskJobRepository callTaskJobRepository;
    private final LineAssignRepository lineAssignRepository;
    private final CallTaskStatisticsRepository callTaskStatisticsRepository;
    @Override
    public PageResult<CallTaskDto> queryAll(CallTaskQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallTask> page = callTaskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callTaskMapper::toDto));
    }

    @Override
    public List<CallTaskDto> queryAll(CallTaskQueryCriteria criteria){
        return callTaskMapper.toDto(callTaskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询呼叫任务表
     * 
     * @param id 呼叫任务表主键
     * @return 呼叫任务表
     */
    @Override
    public CallTaskDto selectCallTaskById(Integer id)  {
        return callTaskMapper.toDto(callTaskRepository.findById(id).orElseGet(CallTask::new));
    }


    /**
     * 新增呼叫任务表
     * 
     * @param callTask 呼叫任务表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCallTask(CallTask callTask,Integer deptId) {
        callTask.setDeptId(deptId);
        validCallTask(callTask);
        callTask.setStatus(CallTaskEnum.TaskStatus.ENABLE.getStatus());
        callTask.setCallStatus(CallTaskEnum.TaskCallStatus.NOT_START.getStatus());
        callTask.setTotalCustomers(0);
        callTask.setCalledCustomers(0);
        callTask.setConnectCount(0);
        callTask.setTodayCustomers(0);
        callTask.setTodayCalled(0);
        callTask.setTodayConnect(0);
        callTaskRepository.save(callTask);
        generateTaskJob(callTask);
    }

    /**
     * 修改呼叫任务表
     * 
     * @param callTask 呼叫任务表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallTask(CallTask callTask,Integer deptId) {
        callTask.setDeptId(deptId);
        validCallTask (callTask);
        CallTask task = callTaskRepository.findById(callTask.getId()).orElseThrow(()-> new BadRequestException("记录不存在"));
        if (!(task.getAllowDay().equals(callTask.getAllowDay()) && task.getAllowTime().equals(callTask.getAllowTime())
                && task.getDenyTime().equals(callTask.getDenyTime()))) {
            //时间及拨打周期不一样，需要修改xxl job
            //移除原有的
            removeTaskJob(task.getId());
            generateTaskJob(callTask);
        }
        //不要使用bean utils copy ，因为有些字段不需要更新，而且callTask默认为空的字段也会被更新
        task.setTemplateName(callTask.getTemplateName());
        task.setCallTemplateId(callTask.getCallTemplateId());
        task.setTaskName(callTask.getTaskName());
        task.setLineId(callTask.getLineId());
        task.setLineConcurrent(callTask.getLineConcurrent());
        task.setAllowTime(callTask.getAllowTime());
        task.setDenyTime(callTask.getDenyTime());
        task.setAllowDay(callTask.getAllowDay());
        task.setRemark(callTask.getRemark());
        task.setRetryOpen(callTask.getRetryOpen());
        task.setRetryCount(callTask.getRetryCount());
        task.setRetryInterval(callTask.getRetryInterval());
        task.setRetryCondition(callTask.getRetryCondition());
        task.setEnableSendSms(callTask.getEnableSendSms());
        task.setSmsTemplateId(callTask.getSmsTemplateId());
        callTaskRepository.save(task);
    }

    /**
     * 批量删除呼叫任务表
     * 
     * @param ids 需要删除的呼叫任务表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTaskByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallTaskById(id);
            removeTaskJob(id);
        }
    }

    /**
     * 删除呼叫任务表信息
     * 
     * @param id 呼叫任务表主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTaskById(Integer id) {
        callTaskRepository.logicDeleteById(id);
        removeTaskJob(id);
    }


    @Override
    public void download(List<CallTaskDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallTaskDto callTask : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callTask.getId());
            map.put("taskName",  callTask.getTaskName());
            map.put("callTemplateId",  callTask.getCallTemplateId());
            map.put("templateName",  callTask.getTemplateName());
            map.put("status",  callTask.getStatus());
            map.put("callStatus",  callTask.getCallStatus());
            map.put("totalCustomers",  callTask.getTotalCustomers());
            map.put("calledCustomers",  callTask.getCalledCustomers());
            map.put("todayCustomers",  callTask.getTodayCustomers());
            map.put("todayCalled",  callTask.getTodayCalled());
            map.put("todayConnect",  callTask.getTodayConnect());
            map.put("allowTime",  callTask.getAllowTime());
            map.put("allowDay",  callTask.getAllowDay());
            map.put("lineId",  callTask.getLineId());
            map.put("lineConcurrent",  callTask.getLineConcurrent());
            map.put("retryOpen",  callTask.getRetryOpen());
            map.put("retryCondition",  callTask.getRetryCondition());
            map.put("retryCount",  callTask.getRetryCount());
            map.put("remark",  callTask.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void switchTaskStatus(CallTask callTask) {
        Integer status = callTask.getStatus();
        if (status==null || callTask.getId()==null) {
            throw new BadRequestException("参数错误");
        }
        callTask = callTaskRepository.findById(callTask.getId()).orElseThrow(()-> new BadRequestException("记录不存在"));
        if (CallTaskEnum.TaskCallStatus.PAUSE.getStatus().equals(callTask.getCallStatus())) {
            throw new BadRequestException("任务正在暂停中，无法进行此操作");
        }
        if (callTask.getStatus().equals(status)) {
            //任务已经是目标状态，无需更改
            return;
        }
        String content = CallTaskEnum.TaskStatus.ENABLE.getStatus().equals(status)?"开启任务":"停止任务";
        handlerTaskJob(callTask.getId(), status);
        boolean alreadyStart = redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_CALL_START_KEY, callTask.getId());
        if (CallTaskEnum.TaskStatus.ENABLE.getStatus().equals(status) && !alreadyStart) {
            log.info("任务{}没有开启", callTask.getId());
            //校验开启时间和日期，如果不在拨打时间内则定时触发，否则直接拨打，并定时暂停
            boolean allowCall = checkAllowCallTime(callTask);
            if (!allowCall) {
                log.info("任务{}不在拨打时间内，进行定时开启", callTask.getId());
                return;
            }
            //再次判断是否存在待拨打的任务
            long count = callCustomerService.countCustomerByStatusAndTaskId(CallCustomerStatusEnum.CustomerStatus.NOT_CALL, callTask.getId());
            if (count>0) {
                log.info("任务{}存在待拨打的数据，进行开启", callTask.getId());
                callCustomerService.startDispatchCallTask(callTask.getId(),callTask.getCallTemplateId());
            }
        } else if(alreadyStart) {
            //任务暂停 且任务正在拨打中，发送停止拨打的mq消息
            rabbitTemplate.convertAndSend(DispatchKeyConstants.DISPATCH_CALL_TASK_PAUSE_MQ_QUEUE_KEY, callTask.getId());
        }
        callTaskRepository.updateStatusById(callTask.getId(), status);
    }

    @Override
    public List<CallTaskInfo> findAllProcessingTask(Integer status) {
        return callTaskRepository.selectAllTaskByStatus(status);
    }

    @Override
    public Long getTaskCountInfo() {
        return callTaskRepository.count();
    }

    @Override
    public Map<Integer, Long> getTaskCallStatusCountInfo() {
        List<CallStatusCountInfo> list = callTaskRepository.countByStatus();
        return list.stream().collect(Collectors.toMap(CallStatusCountInfo::callStatus, CallStatusCountInfo::count));
    }

    private void removeTaskJob(Integer taskId) {
        List<CallTaskJob> list = callTaskJobRepository.findByTaskId(taskId);
        if (list != null && !list.isEmpty()) {
            list.forEach(s->{
                try {
                    String result = jobService.removeJobInfo(s.getJobId());
                    log.info("删除任务定时任务成功，result:{}", result);
                    callTaskJobRepository.logicDeleteById(s.getId());
                } catch (Exception e) {
                    log.error("删除任务定时任务失败", e);
                }
            });
        }
    }

    private void handlerTaskJob(Integer taskId, Integer status) {
        List<CallTaskJob> list = callTaskJobRepository.findByTaskId(taskId);
        if (list != null && !list.isEmpty()) {
            list.forEach(s->{
                try {
                    String  result = jobService.updateJobStatus(s.getJobId(), status);
                    log.info("更新任务定时任务成功，result:{}", result);
                } catch (Exception e) {
                    log.error("更新任务定时任务状态失败", e);
                }
            });
        }
    }

    private void generateTaskJob(CallTask callTask) {
        List<CronExpressionInfo> list = CronExpressionUtils.generateComplexCron(
                callTask.getAllowDay(),
                callTask.getAllowTime(),callTask.getDenyTime()
        );
        list.forEach(s-> addTaskJob(callTask,s));
    }

    private void addTaskJob(CallTask callTask, CronExpressionInfo cron) {
        String cronExpression = cron.cronExpression();
        int type = cron.type();
        CallTaskJob taskJob = new CallTaskJob();
        taskJob.setTaskId(callTask.getId());
        taskJob.setCronExpression(cronExpression);
        taskJob.setExecutorHandler(DispatchKeyConstants.DISPATCH_CALL_TASK_BEAN_HANDLE);
        taskJob.setType(type);
        String jobDesc  ;
        Map<String,Object> params = new HashMap<>();
        params.put("taskId",callTask.getId());
        params.put("type",type);
        if (CronExpressionInfo.Type.START==type) {
            //启动任务
            jobDesc = callTask.getId() + "_" + callTask.getTaskName() + "启动任务";
        }else {
            //停止任务
            jobDesc = callTask.getId() + "_" + callTask.getTaskName() + "停止任务";
        }
        taskJob.setJobDesc(jobDesc);
        taskJob.setJobParams(JsonUtils.toJsonString(params));
        String jobId = addXxlJob(taskJob);
        try {
            taskJob.setJobId(Integer.valueOf(jobId));
            taskJob.setStatus(CallTaskJobStatusEnum.SUCCESS.getValue());
        }catch (Exception e) {
            log.info("xxl job 添加失败，结果是:{}",jobId);
            log.error("xxl job添加失败",e);
            taskJob.setStatus(CallTaskJobStatusEnum.FAIL.getValue());
        }
        callTaskJobRepository.save(taskJob);
    }

    private String addXxlJob(CallTaskJob taskJob) {
        BasicXxlJob xxlJob = new BasicXxlJob(taskJob.getExecutorHandler(),
                xxlJobProperties.getExecutor().getJobGroup(),
                taskJob.getCronExpression(),
                taskJob.getJobParams(), taskJob.getJobDesc(), xxlJobProperties.getExecutor().getAlertEmail());
        try {
            return jobService.addBasicJobHandler(xxlJob);
        } catch (Exception e) {
            throw new BadRequestException( "添加定时任务异常");
        }
    }

    private void validCallTask (CallTask callTask) {
        String taskName = callTask.getTaskName();
        if (StringUtils.isBlank(taskName) || callTask.getCallTemplateId()==null) {
            throw new BadRequestException( "参数不正确");
        }
        if (StringUtils.isBlank(callTask.getAllowDay())) {
            throw new BadRequestException( "拨打周期为空");
        }
        if (StringUtils.isBlank(callTask.getAllowTime())) {
            throw new BadRequestException( "允许拨打时间为空");
        }
        TimeRange allTimeRange = new TimeRange(callTask.getAllowTime());
        boolean valid = allTimeRange.validTimeRange();
        if (!valid) {
            throw new BadRequestException( "允许拨打时间开始时间需要小于结束时间");
        }
        if (StringUtils.isNotBlank(callTask.getDenyTime())) {
            TimeRange denyTimeRange = new TimeRange(callTask.getDenyTime());
            valid = denyTimeRange.validTimeRange();
            if (!valid) {
                throw new BadRequestException( "禁止拨打时间开始时间需要小于结束时间");
            }
            if (!(allTimeRange.contains(denyTimeRange.getStartTime()) && allTimeRange.contains(denyTimeRange.getEndTime()))) {
                throw new BadRequestException( "禁止拨打时间不能大于允许拨打时间");
            }
        }
        CallTask task = callTaskRepository.findByTaskName(taskName);
        if (task!=null && (callTask.getId()==null || !task.getId().equals(callTask.getId())) ) {
            throw new BadRequestException("任务名称已存在");
        }
        TemplateInfo templateInfo = dialogueTemplateApi.findTemplateInfoById(callTask.getCallTemplateId());
        if (templateInfo==null) {
            throw new BadRequestException("模板不存在");
        }
        callTask.setTemplateName(templateInfo.name());
        //校验线路并发
        Integer lineId = callTask.getLineId();
        validLineConcurrent(lineId, callTask.getDeptId(),callTask.getLineConcurrent());
    }

    private void validLineConcurrent(Integer lineId,Integer deptId, Integer concurrent) {
        //获取拨打
        LineAssign lineAssign = lineAssignRepository.findByLineIdAndAssignDeptId(lineId, deptId);
        if (lineAssign==null ) {
            throw new BadRequestException("线路未分配给当前部门");
        }
        if (lineAssign.getConcurrency()<concurrent) {
            throw new BadRequestException("线路并发数不足");
        }
    }

    private boolean checkAllowCallTime(CallTask callTask) {
        return callValidationService.validateCallTime(LocalDateTime.now(), new CallTimeConfig(callTask.getAllowDay(), callTask.getAllowTime(), callTask.getDenyTime()));
    }
}
