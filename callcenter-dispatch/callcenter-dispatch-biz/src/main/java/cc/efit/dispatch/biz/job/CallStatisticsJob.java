package cc.efit.dispatch.biz.job;

import cc.efit.dispatch.biz.service.CallTaskService;
import cc.efit.redis.utils.RedisLock;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.DISPATCH_XXL_JOB_CALL_SYSTEM_STATISTICS_LOCK;
import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.DISPATCH_XXL_JOB_CALL_TASK_STATISTICS_LOCK;

@Component
@Slf4j
public class CallStatisticsJob {
    @Autowired
    private CallTaskService callTaskService;
    @Autowired
    private RedisLock redisLock;

    /**
     * 任务级别统计  9：00-21：00 每 5分钟执行一次
     */
    @XxlJob("callTaskStatistics")
    public void callTaskStatistics() {
        if (!redisLock.tryLockResetExpire(DISPATCH_XXL_JOB_CALL_TASK_STATISTICS_LOCK, 0, TimeUnit.SECONDS)) {
            log.info("callTaskStatistics 已被其他调度实例占用锁，本轮跳过（多实例部署正常行为）");
            return;
        }
        try {
            String params = XxlJobHelper.getJobParam();
            log.info("统计信息定时任务start params:{}", params);
            callTaskService.statistics(params);
            log.info("统计信息定时任务end");
        } finally {
            redisLock.unlock(DISPATCH_XXL_JOB_CALL_TASK_STATISTICS_LOCK);
        }
    }

    /**
     * 系统级别统计 9：00-21：00 1个小时执行一次
     */
    @XxlJob("callSystemStatistics")
    public void callSystemStatistics() {
        if (!redisLock.tryLockResetExpire(DISPATCH_XXL_JOB_CALL_SYSTEM_STATISTICS_LOCK, 0, TimeUnit.SECONDS)) {
            log.info("callSystemStatistics 已被其他调度实例占用锁，本轮跳过（多实例部署正常行为）");
            return;
        }
        try {
            String params = XxlJobHelper.getJobParam();
            log.info("系统级别统计定时任务start params:{}", params);
            callTaskService.systemStatistics(params);
            log.info("系统级别统计定时任务end");
        } finally {
            redisLock.unlock(DISPATCH_XXL_JOB_CALL_SYSTEM_STATISTICS_LOCK);
        }
    }
}
