package cc.efit.dispatch.biz.intelligent.concurrency;

public interface LineConcurrencyManager {
    /**
     * 申请线路并发资源
     */
    int acquire(Integer lineId, Integer deptId, int required);

    /**
     * 初始化线路 信号量
     * @param lineId    线路id
     * @param deptId    部门id
     * @param concurrency 并发数
     */
    void initLineConcurrencySemaphore(Integer lineId,Integer deptId, Integer concurrency );

    /**
     * 释放线路并发资源
     */
    void release(Integer lineId, Integer deptId, int permits);
    /**
     * 释放线路并发资源
     */
    void release(Integer lineId, Integer deptId);
    /**
     * 添加降级任务（用于后续补偿或异步处理）
     */
    void addReduceTask(Integer lineId, Integer deptId, Integer taskId, int reduce);
}