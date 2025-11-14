package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-09-10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CallCustomerQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   taskId ;
    /** 精确 */
    @Query
    private String   phone ;
    /** 精确 */
    @Query
    private Integer   status ;
}