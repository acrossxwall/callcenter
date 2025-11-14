package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CallTemplateQueryCriteria extends BaseQueryReq {
    @Query
    private Integer id;
    /** 精确 */
    @Query(type = Query.Type.INNER_LIKE)
    private String   name ;
    /** 精确 */
    @Query
    private String   industry ;
    /** 精确 */
    @Query
    private Integer   status ;
}