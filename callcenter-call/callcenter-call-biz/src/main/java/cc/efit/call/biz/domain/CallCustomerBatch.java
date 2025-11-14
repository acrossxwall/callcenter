package cc.efit.call.biz.domain;

import cc.efit.db.base.BaseCommonConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 客户名单批次表对象 efit_call_customer_batch
 * 
 * @author across
 * @date 2025-09-10
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_customer_batch")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class CallCustomerBatch extends DataPermissionEntity implements Serializable {

    /** 批次ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 关联任务ID */
    private Integer taskId;
    /** 客户数量 */
    private Integer customerCount;
    /** 成功数量 */
    private Integer successCount;
    /** 失败数量 */
    private Integer failCount;
    /** 来源，1:页面导入，2:api导入 */
    private Integer source;
    /** 批次号 */
    private String batchNo;
    /** 状态 0-未导入 1-导入成功 2-部分成功 3-失败 */
    private Integer status;
}
