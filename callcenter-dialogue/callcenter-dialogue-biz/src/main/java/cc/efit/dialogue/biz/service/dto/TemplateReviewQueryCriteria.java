package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-26
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateReviewQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   callTemplateId ;
    /** 精确 */
    @Query
    private Integer   status ;
    @Query(type = Query.Type.INNER_LIKE)
    private String name;
}