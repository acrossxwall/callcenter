package cc.efit.dispatch.biz.job;

import cc.efit.call.api.vo.line.DispatchLineVo;
import cc.efit.dispatch.biz.intelligent.IntelligentAdjustConcurrent;
import cc.efit.dispatch.biz.service.CallTaskService;
import cc.efit.redis.utils.RedisLock;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.DISPATCH_XXL_JOB_CHECK_CALL_TASK_STATUS_LOCK;
import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.DISPATCH_XXL_JOB_DISPATCH_CALL_TASK_LOCK;

@Component
@Slf4j
public class CallTaskDispatchJob {
    @Autowired
    private CallTaskService callTaskService;
    @Autowired
    private IntelligentAdjustConcurrent intelligentAdjustConcurrent;
    @Autowired
    private RedisLock redisLock;

    @XxlJob("dispatchCallTask")
    public void dispatchCallTask() {
        if (!redisLock.tryLockResetExpire(DISPATCH_XXL_JOB_DISPATCH_CALL_TASK_LOCK, 0, TimeUnit.SECONDS)) {
            log.info("dispatchCallTask 已被其他调度实例占用锁，本轮跳过（多实例部署正常行为）");
            return;
        }
        try {
            log.info("调度并发任务start");
            log.info("先检测暂停任务");
            callTaskService.checkPauseTaskStatus();
            log.info("检测暂停任务结束");
            List<DispatchLineVo> lineVos = callTaskService.findRunningDispatchLineVoList();
            if (lineVos==null || lineVos.isEmpty()) {
                log.info("没有需要调度的拨打任务");
                return;
            }
            Set<DispatchLineVo> set = new HashSet<>(lineVos);
            intelligentAdjustConcurrent.adjustConcurrent(set);
            log.info("调度并发任务end");
        } finally {
            redisLock.unlock(DISPATCH_XXL_JOB_DISPATCH_CALL_TASK_LOCK);
        }
    }
    @XxlJob("checkCallTaskStatus")
    public void checkCallTaskStatus() {
        if (!redisLock.tryLockResetExpire(DISPATCH_XXL_JOB_CHECK_CALL_TASK_STATUS_LOCK, 0, TimeUnit.SECONDS)) {
            log.info("checkCallTaskStatus 已被其他调度实例占用锁，本轮跳过（多实例部署正常行为）");
            return;
        }
        try {
            callTaskService.checkCallTaskStatus();
        } finally {
            redisLock.unlock(DISPATCH_XXL_JOB_CHECK_CALL_TASK_STATUS_LOCK);
        }
    }
}
