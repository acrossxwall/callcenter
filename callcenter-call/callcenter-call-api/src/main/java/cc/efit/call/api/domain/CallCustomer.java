package cc.efit.call.api.domain;

import cc.efit.data.permission.DataPermissionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Map;

/**
 * 客户名单表对象 efit_call_customer
 * 
 * @author across
 * @date 2025-09-10
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_customer")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class CallCustomer extends DataPermissionEntity implements Serializable {

    /** 客户ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 关联任务ID */
    private Integer taskId;
    /** 电话号码 */
    private String phone;
    /** 批次id */
    private Integer batchId;
    /** 批次号 */
    private String batchNo;
    /** 呼叫caseId */
    private String caseId;
    /** 呼叫状态：0-待呼叫 1-已加载 2-呼叫中 3-已完成 */
    private Integer status;
    /** 0-未接听 1-已接听 */
    private Integer calledStatus;
    /** 有值转接坐席 1-已接听 0-未接听 */
    private Integer transferStatus;
    /** 客户字典 */
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String,String> customerInfo;
    /** 开放平台id */
    private String appId;
    /** 呼叫id */
    private String callId;
}
