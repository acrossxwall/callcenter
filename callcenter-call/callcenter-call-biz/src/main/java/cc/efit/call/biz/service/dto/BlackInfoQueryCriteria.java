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
public class BlackInfoQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private String   name ;
    /** 精确 */
    @Query
    private String   phone ;
}