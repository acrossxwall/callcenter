package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-09-12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CallCustomerImportDetailQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   taskId ;
    /** 精确 */
    @Query
    private Integer   batchId ;
    /** 精确 */
    @Query
    private String   batchNo ;
}