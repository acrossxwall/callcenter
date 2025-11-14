package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CallTaskQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   id ;
    /** 精确 */
    @Query
    private String   taskName ;
    /** 精确 */
    @Query
    private Integer   lineId ;
}