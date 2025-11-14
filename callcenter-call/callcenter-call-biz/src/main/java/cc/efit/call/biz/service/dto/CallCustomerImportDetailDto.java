package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 客户名单批次详细表对象 efit_call_customer_import_detail
 * 
 * @author across
 * @date 2025-09-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallCustomerImportDetailDto extends BaseDTO implements Serializable {

    /** 主键 */
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
