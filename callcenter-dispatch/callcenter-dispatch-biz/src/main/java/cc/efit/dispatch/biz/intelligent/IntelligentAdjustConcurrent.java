package cc.efit.dispatch.biz.intelligent;

import cc.efit.call.api.vo.line.DispatchLineVo;

import java.util.Set;

public interface IntelligentAdjustConcurrent {

    default void adjustConcurrent(Set<DispatchLineVo> set){
        set.forEach(line -> adjustConcurrent(line.lineId(), line.deptId()));
    }
    /**
     * 调整并发 线路是分配到部门的所以按照部门加线路id
     * @param lineId    线路id
     * @param deptId    部门id
     */
    void adjustConcurrent(Integer lineId, Integer deptId);

    /**
     * 任务并发调控减少时 释放线路并发
     * @param lineId    线路id
     * @param deptId    部门id
     * @param taskId    任务id
     */
    void releaseLineReduceConcurrent(Integer lineId, Integer deptId, Integer taskId);

    /**
     * 暂停的任务释放线路并发
     * @param taskId        任务id
     * @return              true释放成功 false未释放
     */
    boolean releaseLinePauseConcurrent(Integer taskId);


    /**
     * 完成的任务释放线路并发
     * @param taskId        任务id
     * @return              true释放成功 false未释放
     */
    boolean releaseTaskFinishConcurrent(Integer taskId);

    int findTaskAdjustConcurrent(Integer taskId);
}
