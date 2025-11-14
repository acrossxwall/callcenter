package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class LineAssignQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   lineId ;
}