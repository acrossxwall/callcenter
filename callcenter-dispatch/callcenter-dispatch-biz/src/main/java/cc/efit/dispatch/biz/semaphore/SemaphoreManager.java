package cc.efit.dispatch.biz.semaphore;

public interface SemaphoreManager {
    /**
     * 设置信号量的信号数
     */
    void setPermits(String key, int permits);

    /**
     * 增加/减少信号量信号（正数为增加，负数为减少）
     */
    void addPermits(String key, int permits);

    /**
     * 信号量不存在set，存在调用add
     * @param key       信号量key
     * @param permits   许可
     */
    void setOrAddPermits(String key, int permits);

    /**
     * 获取当前可用信号数
     */
    int availablePermits(String key);

    void release(String key,int permits);

    /**
     * 获取信号量信号，如果不足，则返回可用的所有信号量 Math.min(availablePermits, permits)
     * @param key       信号量 key
     * @param permits   请求的信号量信号数
     * @return          获取到的信号量信号数
     */
    int acquirePermits(String key, int permits);
}