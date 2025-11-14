package cc.efit.dispatch.biz.intelligent.handler;

import cc.efit.call.api.constants.DispatchKeyConstants;
import cc.efit.call.api.domain.CallTask;
import cc.efit.call.api.domain.LineAssign;
import cc.efit.call.api.enums.CallCustomerStatusEnum;
import cc.efit.call.api.repository.CallTaskRepository;
import cc.efit.call.api.repository.LineAssignRepository;
import cc.efit.call.api.vo.task.CallTaskConcurrency;
import cc.efit.dispatch.api.constant.DispatchRedisKeyConstant;
import cc.efit.dispatch.biz.intelligent.AllocationStrategy;
import cc.efit.dispatch.biz.intelligent.IntelligentAdjustConcurrent;
import cc.efit.dispatch.biz.intelligent.concurrency.LineConcurrencyManager;
import cc.efit.dispatch.biz.intelligent.context.AssignContext;
import cc.efit.dispatch.biz.intelligent.utils.FormatRedisKeyUtils;
import cc.efit.dispatch.biz.semaphore.SemaphoreManager;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisLock;
import cc.efit.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.*;

@Slf4j
@Service
public class IntelligentAdjustConcurrentHandler implements IntelligentAdjustConcurrent {
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private LineAssignRepository lineAssignRepository;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CallTaskRepository callTaskRepository;
    @Autowired
    private AllocationStrategy allocationStrategy;
    @Autowired
    private LineConcurrencyManager lineConcurrencyManager;
    @Autowired
    private SemaphoreManager semaphoreManager;
    @Autowired
    private Redisson redisson;

    @Override
    public void adjustConcurrent(Integer lineId, Integer deptId) {
        log.info("调整并发:lineId:{},deptId:{}",lineId,deptId);
        String lockKey = FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_CONCURRENT_LOCK,lineId,deptId);
        boolean adjustLock = false;
        try {
            adjustLock = redisLock.tryLockResetExpire(lockKey, 10, TimeUnit.SECONDS);
            if (!adjustLock) {
                log.info("调整并发加锁失败，有其他线程在调整,lineId:{},deptId:{}",lineId,deptId);
                return;
            }
            //查询分配给当前dept的总并发
            LineAssign lineAssign = lineAssignRepository.findByLineIdAndAssignDeptId(lineId,deptId);
            if (lineAssign==null || lineAssign.getConcurrency()==null || lineAssign.getConcurrency()==0) {
                log.info("调整并发失败，当前线路没有分配给当前部门,lineId:{},deptId:{}",lineId,deptId);
                return;
            }
            int concurrency = lineAssign.getConcurrency();
            //初始化线路并发
            lineConcurrencyManager.initLineConcurrencySemaphore(lineId,deptId,concurrency);
            log.info("开始调整并发lineId:{},deptId:{},concurrency:{}",lineId,deptId,concurrency);
            //查询当前线路当前部门执行中的任务，查询数据库
            List<CallTaskConcurrency> taskList = callTaskRepository.selectRunningTaskByStatus(lineId,deptId);
            if (taskList==null || taskList.isEmpty()) {
                log.info("调整并发失败，当前线路当前部门没有执行中的任务,lineId:{},deptId:{}",lineId,deptId);
                return;
            }
            boolean assign = allocationStrategy.allocateCallTask(taskList,concurrency);
            if (assign){
                log.info("调整并发成功,lineId:{},deptId:{},concurrency:{}",lineId,deptId,concurrency);
            }else{
                //TODO 判断进入重呼是否要减并发
            }
            taskList.forEach(s-> assignConcurrency(s, lineId, deptId));
            boolean assignSuccess = taskList.stream().anyMatch(s-> s.getAssign().equals(s.getConcurrency()));
            log.info("调整并发结束,lineId:{},deptId:{},concurrency:{},assignSuccess:{}",lineId,deptId,concurrency,assignSuccess);
            redisUtils.set(FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_CONCURRENT_INFO, lineId, deptId), JsonUtils.toJsonString(taskList));
        }finally {
            if (adjustLock) {
                redisLock.unlock(lockKey);
            }
        }
    }


    @Override
    public void releaseLineReduceConcurrent(Integer lineId, Integer deptId, Integer taskId) {
        //大多数任务时不需要减少的，所以现进行判断
        boolean reduceTask = checkLineReduceTask(lineId, taskId, deptId);
        if (!reduceTask) {
            return;
        }
        String reduceLineTaskLock =  DISPATCH_ADJUST_LINE_REDUCE_TASK_LOCK.formatted(lineId,taskId);
        try {
            redisLock.lock(reduceLineTaskLock );
            log.info("lineId:{} reduce task:{} ",lineId,taskId );
            //进入锁内部后再次判断
            reduceTask = checkLineReduceTask(lineId, taskId, deptId);
            if (!reduceTask) {
                return;
            }
            String reduceTaskKey = DISPATCH_ADJUST_LINE_REDUCE_TASK_SEMAPHORE.formatted(lineId,taskId);
            RSemaphore reduceTaskSemaphore = redisson.getSemaphore(reduceTaskKey) ;
            int available  = reduceTaskSemaphore.availablePermits();
            if (available>=0) {
                //说明reduce enough
                log.info("lineId:{} reduce task:{} enough ,available:{} ",lineId,taskId,available);
                removeLineReduceTask(lineId,taskId,deptId );
                reduceTaskSemaphore.delete();
                return;
            }
            reduceTaskSemaphore.release();
            lineConcurrencyManager.release(lineId,deptId);
        }finally {
            redisLock.unlock(reduceLineTaskLock);
        }

    }

    @Override
    public boolean releaseLinePauseConcurrent(Integer taskId) {
        String taskAssignInfo = DISPATCH_TASK_ASSIGN_INFO_KEY.formatted(taskId);
        CallTaskConcurrency task = redisUtils.get(taskAssignInfo,CallTaskConcurrency.class);
        if (task==null) {
            return true;
        }
        String concurrencyKey =  DispatchRedisKeyConstant.DISPATCH_TASK_CONCURRENCY.formatted(taskId);
        int available = semaphoreManager.availablePermits(concurrencyKey);
        int reduce = task.getConcurrency() - available;
        if (reduce<=0) {
            log.info("暂停中的任务，没有正在呼叫的客户，taskId:{}",taskId);
            clearTaskInfo(taskId);
            //释放暂停任务的线路并发
            lineConcurrencyManager.release(task.getLineId(),task.getDeptId(),task.getConcurrency());
            return true;
        }
        return false;
    }


    @Override
    public boolean releaseTaskFinishConcurrent(Integer taskId) {
        String taskAssignInfo = DISPATCH_TASK_ASSIGN_INFO_KEY.formatted(taskId);
        CallTaskConcurrency task = redisUtils.get(taskAssignInfo,CallTaskConcurrency.class);
        if (task==null) {
            return true;
        }
        clearTaskInfo(taskId);
        //释放暂停任务的线路并发
        lineConcurrencyManager.release(task.getLineId(),task.getDeptId(),task.getConcurrency());
        //再分配一次任务
        adjustConcurrent(task.getLineId(), task.getDeptId());
        return true;
    }

    @Override
    public int findTaskAdjustConcurrent(Integer taskId) {
        String taskAssignInfo = DISPATCH_TASK_ASSIGN_INFO_KEY.formatted(taskId);
        CallTaskConcurrency task = redisUtils.get(taskAssignInfo, CallTaskConcurrency.class);
        return task==null || task.getConcurrency()==null?0: task.getConcurrency();
    }

    private void clearTaskInfo(Integer taskId) {
        String key = DISPATCH_TASK_INFO_KEY.formatted(taskId);
        redisUtils.del(key);
        String concurrentKey =  DispatchRedisKeyConstant.DISPATCH_TASK_CONCURRENCY.formatted(taskId);
        redisUtils.del(concurrentKey);
    }

    private void removeLineReduceTask(Integer lineId, Integer deptId,Integer taskId) {
        String lineKey = FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_LINE_REDUCE , lineId,deptId);
        redisUtils.setRemove(lineKey,taskId);
    }

    private boolean checkLineReduceTask(Integer lineId, Integer deptId,Integer taskId) {
        String lineKey = FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_LINE_REDUCE , lineId,deptId);
        return redisUtils.sHasKey(lineKey,taskId );
    }

    private void assignConcurrency(CallTaskConcurrency task, Integer lineId, Integer deptId) {
        Integer taskId = task.getTaskId();
        String taskAssignInfo = DISPATCH_TASK_ASSIGN_INFO_KEY.formatted(taskId);
        CallTaskConcurrency lastTask = redisUtils.get(taskAssignInfo, CallTaskConcurrency.class);
        task.setLineId(lineId);
        AssignContext ctx = buildAssignContext(task, lastTask, lineId);
        if (ctx.firstAssign()) {
            handleFirstAssign(lineId, deptId, task);
        }else if (ctx.concurrencyChanged()) {
            handleConcurrencyChanged(ctx, lineId, deptId, task);
        }else if (ctx.lineChanged()) {
            handleLineChanged( lineId, deptId, task);
        }
        redisUtils.set(taskAssignInfo, JsonUtils.toJsonString(task));
    }


    private void handleFirstAssign(Integer lineId, Integer deptId, CallTaskConcurrency task) {
        Integer newConcurrency = task.getConcurrency();
        Integer taskId = task.getTaskId();
        String concurrencyKey = DISPATCH_TASK_CONCURRENCY.formatted(taskId);
        log.info("taskId:{},之前未拨打第一次分配并发数:{}",taskId,newConcurrency);
        int available = lineConcurrencyManager.acquire(lineId, deptId, newConcurrency);
        log.info("taskId:{},第一次分配并发数:{},实际获取线路并发数:{}",taskId,newConcurrency,available);
        if (available>0) {
            semaphoreManager.setPermits(concurrencyKey, available);
        }
        task.setConcurrency(available);
    }

    private void handleConcurrencyChanged(AssignContext ctx, Integer lineId, Integer deptId, CallTaskConcurrency task) {
        Integer lastConcurrency = ctx.lastConcurrency();
        Integer taskId = task.getTaskId();
        Integer newConcurrency = task.getConcurrency();
        log.info("taskId:{},之前有拨打,旧的并发数:{},新的并发数:{}",taskId,lastConcurrency ,newConcurrency);
        String concurrencyKey = DISPATCH_TASK_CONCURRENCY.formatted(taskId);
        int delta  = newConcurrency - lastConcurrency;
        log.info("taskId:{},之前有拨打,旧的并发数:{},新的并发数:{},需要调整的并发数:{}",
                taskId,lastConcurrency ,newConcurrency,delta );
        if (delta>0) {
            int available = lineConcurrencyManager.acquire(lineId, deptId, delta);
            log.info("taskId:{},需要调整的并发数:{},实际获取线路并发数:{}",taskId,delta,available);
            task.setConcurrency(lastConcurrency + available);
            if (available>0) {
                semaphoreManager.addPermits(concurrencyKey, available);
            }
        }else{
            //该任务优先级降低或者并发调低了
            semaphoreManager.addPermits(concurrencyKey, delta);
            //判断降低的并发是否已经全部回收
            int available = semaphoreManager.availablePermits(concurrencyKey);
            if (available>=0) {
                lineConcurrencyManager.release(lineId, deptId, -delta);
            }else{
                int sub = available - delta;
                if (sub>0) {
                    lineConcurrencyManager.release(lineId, deptId, sub);
                }
                //取 available 和 reduce 二者较大值
                lineConcurrencyManager.addReduceTask(lineId,deptId,taskId,Math.max(available, delta));
            }
        }
    }

    /**
     * 并发一样线路更改
     */
    private void handleLineChanged(Integer lineId, Integer deptId, CallTaskConcurrency task) {
        Integer taskId = task.getTaskId();
        Integer newConcurrency = task.getConcurrency();
        int available = lineConcurrencyManager.acquire(lineId, deptId, newConcurrency);
        log.info("换了线路，lineId:{},taskId:{},需要调整的并发数:{},可用并发数:{} ",lineId,taskId,newConcurrency,available);
        String concurrencyKey = DISPATCH_TASK_CONCURRENCY.formatted(taskId);
        if (available>0) {
            semaphoreManager.addPermits(concurrencyKey, available);
        }
        task.setConcurrency(available);
    }


    private AssignContext buildAssignContext(CallTaskConcurrency task, CallTaskConcurrency lastTask, Integer lineId) {
        Integer newConcurrency = task.getConcurrency();
        int lastConcurrency = lastTask != null ? lastTask.getConcurrency() : 0;
        Integer oldLineId = lastTask != null ? lastTask.getLineId() : -1;

        boolean first =  lastTask == null ;
        boolean lineChanged = !lineId.equals(oldLineId);
        boolean concurrencyChanged = !newConcurrency.equals(lastConcurrency);
        lastConcurrency = lineChanged ? 0: lastConcurrency;
        return new AssignContext(
                task.getTaskId(),
                newConcurrency,
                lastConcurrency,
                lineId,
                oldLineId,
                first,
                concurrencyChanged,
                lineChanged
        );
    }
}
