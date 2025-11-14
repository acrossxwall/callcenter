package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.Date;
/**
 * 外呼任务job表对象 efit_call_task_job
 * 
 * @author across
 * @date 2025-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallTaskJobDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 任务ID */
    private Integer taskId;

    /** cron表达式 */
    private String cronExpression;

    /** 执行器名称 */
    private String executorHandler;

    /** 描述 */
    private String jobDesc;

    /** 任务类型：1:开启，2:暂停 */
    private Integer type;

    /** 状态：1:成功，2:失败 */
    private Integer status;

    /** xxlJob的ID */
    private Integer jobId;

}
