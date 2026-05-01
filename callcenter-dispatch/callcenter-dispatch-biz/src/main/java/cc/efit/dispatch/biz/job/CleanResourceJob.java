package cc.efit.dispatch.biz.job;

import cc.efit.call.api.constants.DispatchKeyConstants;
import cc.efit.dispatch.api.constant.CallRecordConstant;
import cc.efit.redis.utils.RedisLock;
import cc.efit.redis.utils.RedisUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.DISPATCH_XXL_JOB_RELEASE_CALL_RESOURCE_LOCK;

/**
 * 清空调度侧 Redis 状态（多用于凌晨释放）。多实例下仅抢锁成功的节点执行一次，避免并发重复 scan/del。
 */
@Slf4j
@Component
public class CleanResourceJob {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedisLock redisLock;

    @XxlJob("releaseCallResource")
    public void releaseCallResource() {
        if (!redisLock.tryLockResetExpire(DISPATCH_XXL_JOB_RELEASE_CALL_RESOURCE_LOCK, 0, TimeUnit.SECONDS)) {
            log.info("releaseCallResource 已被其他调度实例占用锁，本轮跳过");
            return;
        }
        try {
            log.warn("releaseCallResource 开始执行 Redis 清理");
            redisUtils.del(CallRecordConstant.CALL_RECORD_SET_KEY,
                    DispatchKeyConstants.DISPATCH_CALL_START_KEY,DispatchKeyConstants.DISPATCH_REDIS_LOAD_DATA);
            redisUtils.scanDel("dispatch:task:*");
            redisUtils.scanDel("dispatch:adjust:*");
            log.info("释放资源end");
        } finally {
            redisLock.unlock(DISPATCH_XXL_JOB_RELEASE_CALL_RESOURCE_LOCK);
        }
    }
}
