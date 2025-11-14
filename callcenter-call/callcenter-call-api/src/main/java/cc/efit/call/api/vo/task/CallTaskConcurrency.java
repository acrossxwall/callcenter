package cc.efit.call.api.vo.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallTaskConcurrency {
    /**
     * 任务id
     */
    private Integer taskId;
    /**
     * 数据库设置线路并发数
     */
    private Integer lineConcurrent;
    /**
     * 调控并发实际分配的并发数
     */
    private Integer concurrency;
    /**
     * 计算分配数
     */
    private Integer assign;
    /**
     * 任务优先级
     */
    private Integer priority;
    /**
     * 线路id
     */
    private Integer lineId;
    /**
     * 部门id
     */
    private Integer deptId;

    public CallTaskConcurrency(){}
    public CallTaskConcurrency(Integer taskId,Integer lineConcurrent,Integer priority){
        this.taskId = taskId;
        this.lineConcurrent = lineConcurrent;
        this.priority  = priority;
    }
}
