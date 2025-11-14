package cc.efit.dispatch.biz.intelligent.concurrency;

import cc.efit.dispatch.biz.intelligent.utils.FormatRedisKeyUtils;
import cc.efit.dispatch.biz.semaphore.SemaphoreManager;
import cc.efit.redis.utils.RedisLock;
import cc.efit.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLineConcurrencyManager implements LineConcurrencyManager {

    private final RedisUtils redisUtils;
    private final RedisLock redisLock;
    private final SemaphoreManager semaphoreManager;

    @Override
    public int acquire(Integer lineId, Integer deptId, int acquire) {
        String lineConcurrentKey = FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_CONCURRENT, lineId,deptId);
        return semaphoreManager.acquirePermits(lineConcurrentKey,acquire);
    }
    @Override
    public void initLineConcurrencySemaphore(Integer lineId,Integer deptId, Integer concurrency ) {
        String lastConcurrencyKey = FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_CONCURRENT_LAST, lineId,deptId);
        Integer last = null;
        Object obj = redisUtils.get(lastConcurrencyKey);
        if (obj != null) {
            last = Integer.valueOf(obj.toString());
        }
        String lineConcurrentKey = FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_CONCURRENT, lineId,deptId);
        if (last == null) {
            semaphoreManager.setPermits(lineConcurrentKey, concurrency);
        }else if(!last.equals(concurrency)){
            semaphoreManager.addPermits(lineConcurrentKey,concurrency - last);
        }
        redisUtils.set(lastConcurrencyKey,concurrency);
    }

    @Override
    public void release(Integer lineId, Integer deptId, int permits) {
        log.info("需要释放lineId:{},deptId:{},并发量:{}",lineId,deptId,permits);
        String lineConcurrentKey = FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_CONCURRENT, lineId,deptId);
        semaphoreManager.release(lineConcurrentKey, permits);
    }

    @Override
    public void release(Integer lineId, Integer deptId){
        release(lineId,deptId,1);
    }

    @Override
    public void addReduceTask(Integer lineId, Integer deptId, Integer taskId, int reduce) {
        String reduceLineTaskLock =  DISPATCH_ADJUST_LINE_REDUCE_TASK_LOCK.formatted(lineId,taskId);
        try {
            redisLock.lock(reduceLineTaskLock);
            String lineKey = FormatRedisKeyUtils.formatIntelligentConcurrent(DISPATCH_ADJUST_LINE_REDUCE , lineId,deptId);
            redisUtils.sSet(lineKey,taskId);
            String reduceTaskKey = DISPATCH_ADJUST_LINE_REDUCE_TASK_SEMAPHORE.formatted(lineId,taskId);
            semaphoreManager.setOrAddPermits(reduceTaskKey,reduce);
        } catch (Exception e) {
            log.error("减并发失败", e);
        } finally {
            redisLock.unlock(reduceLineTaskLock);
        }
    }
}