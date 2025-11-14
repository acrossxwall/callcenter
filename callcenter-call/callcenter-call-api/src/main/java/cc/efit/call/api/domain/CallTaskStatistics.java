package cc.efit.call.api.domain;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 呼叫任务统计表表对象 efit_call_task_statistics
 * 
 * @author across
 * @date 2025-10-15
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_task_statistics")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class CallTaskStatistics extends DataPermissionEntity implements Serializable {

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 任务id */
    private Integer taskId;
    /** 统计时间 */
    private LocalDate callDate;
    /** 导入数 */
    private Integer totalCustomers;
    /** 呼叫数 */
    private Integer calledCustomers;
    /** 接通数 */
    private Integer connectCount;
    /** 通话时长 */
    private Integer duration;
    /** 短信发送量 */
    private Integer smsCount;
    /** 短信成功量 */
    private Integer smsSuccessCount;
    /** 短信计费量 */
    private Integer smsBillCount;
}
