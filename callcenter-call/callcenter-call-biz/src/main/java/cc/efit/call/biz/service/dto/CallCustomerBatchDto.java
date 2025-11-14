package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 客户名单批次表对象 efit_call_customer_batch
 * 
 * @author across
 * @date 2025-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallCustomerBatchDto extends BaseDTO implements Serializable {

    /** 批次ID */
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

}
