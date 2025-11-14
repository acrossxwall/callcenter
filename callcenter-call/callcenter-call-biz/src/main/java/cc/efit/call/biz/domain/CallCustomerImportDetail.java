package cc.efit.call.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 客户名单批次详细表对象 efit_call_customer_import_detail
 * 
 * @author across
 * @date 2025-09-12
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_customer_import_detail")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class CallCustomerImportDetail extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 关联任务ID */
    private Integer taskId;
    /** 手机号 */
    private String phone;
    /** 批次id */
    private Integer batchId;
    /** 批次号 */
    private String batchNo;
    /** 原因 */
    private String reason;
}
