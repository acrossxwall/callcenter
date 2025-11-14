package cc.efit.call.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 外呼任务job表对象 efit_call_task_job
 * 
 * @author across
 * @date 2025-10-20
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_task_job")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class CallTaskJob extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    /** xxlJob的参数 */
    private String jobParams;
}
