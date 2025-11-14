package cc.efit.redis.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class RedisLock {

    private RedissonClient redissonClient;

    public RedisLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
    /**
     * 获取锁
     *
     * @param lockKey 锁实例key
     * @return 锁信息
     */
    public RLock getRLock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }

    /**
     * 加锁（阻塞方法）
     * 一直阻塞，直到获取到锁
     *
     * @param lockKey 锁实例key
     * @return 锁信息
     */
    public RLock lock(String lockKey) {
        RLock lock = getRLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 加锁
     * 尝试获取锁，没获取到锁,直接返回false
     *
     * @param lockKey   锁实例key
     * @param leaseTime 上锁后自动释放锁时间
     * @return true=成功；false=失败
     */
    public Boolean tryLock(String lockKey, long leaseTime) {
        return tryLock(lockKey, 0, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * 加锁
     * 无自动续期
     *
     * @param lockKey   锁实例key
     * @param leaseTime 上锁后自动释放锁时间
     * @param unit      时间颗粒度
     * @return true=加锁成功；false=加锁失败
     */
    public boolean tryLock(String lockKey, long leaseTime, TimeUnit unit) {
        return tryLock(lockKey, 0, leaseTime, unit);
    }

    /**
     * 加锁
     *
     * @param lockKey   锁实例key
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @param unit      时间颗粒度
     * @return true=加锁成功；false=加锁失败
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock rLock = getRLock(lockKey);
        boolean tryLock = false;
        try {
            tryLock = rLock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
        return tryLock;
    }

    /**
     * 加锁
     * 不指定锁的过期时间
     * 锁的过期时间 自动续期
     *
     * @param lockKey   锁实例key
     * @param waitTime  最多等待时间
     * @param unit      时间颗粒度
     * @return true=加锁成功；false=加锁失败
     */
    public boolean tryLockResetExpire(String lockKey, long waitTime, TimeUnit unit) {
        RLock rLock = getRLock(lockKey);
        boolean tryLock = false;
        try {
            tryLock = rLock.tryLock(waitTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
        return tryLock;
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁实例key
     */
    public void unlock(String lockKey) {
        try {
            RLock lock = getRLock(lockKey);
            lock.unlock();
        } catch (Exception e) { }
    }

    /**
     * 释放锁
     *
     * @param lock 锁信息
     */
    public void unlock(RLock lock) {
        try {
            lock.unlock();
        } catch (Exception e) { }
    }
}