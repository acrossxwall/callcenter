package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.Map;

/**
 * 客户名单表对象 efit_call_customer
 * 
 * @author across
 * @date 2025-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallCustomerDto extends BaseDTO implements Serializable {

    /** 客户ID */
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

    /** 呼叫状态：0-待呼叫 1-呼叫中 2-已呼叫 */
    private Integer status;

    /** 0-未接听 1-已接听 */
    private Integer calledStatus;

    /** 有值转接坐席 1-已接听 0-未接听 */
    private Integer transferStatus;

    /** 客户字典 */
    private Map<String,String> customerInfo;

    /** 开放平台id */
    private String appId;

}
