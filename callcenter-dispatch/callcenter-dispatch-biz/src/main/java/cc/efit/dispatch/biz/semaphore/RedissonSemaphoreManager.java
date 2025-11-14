package cc.efit.dispatch.biz.semaphore;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedissonSemaphoreManager implements SemaphoreManager {

    private final RedissonClient redisson;

    @Override
    public void setPermits(String key, int permits) {
        RSemaphore semaphore = redisson.getSemaphore(key);
        semaphore.trySetPermits(permits);
    }

    @Override
    public void addPermits(String key, int permits) {
        RSemaphore semaphore = redisson.getSemaphore(key);
        semaphore.addPermits(permits);
    }

    @Override
    public void setOrAddPermits(String key, int permits) {
        RSemaphore semaphore = redisson.getSemaphore(key) ;
        if (semaphore.isExists()){
            semaphore.addPermits(permits);
        }else{
            semaphore.trySetPermits(permits);
        }
    }

    @Override
    public int availablePermits(String key) {
        RSemaphore semaphore = redisson.getSemaphore(key);
        return semaphore.availablePermits();
    }

    @Override
    public void release(String key, int permits) {
        RSemaphore semaphore = redisson.getSemaphore(key);
        semaphore.release(permits);
    }

    @Override
    public int acquirePermits(String key, int permits) {
        RSemaphore semaphore = redisson.getSemaphore(key) ;
        int available = semaphore.availablePermits();
        int result = Math.min(permits, available);
        if (result>0) {
            semaphore.tryAcquire(result);
        }
        return  Math.max(result, 0);
    }
}