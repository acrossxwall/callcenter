package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-16
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateKnowledgeQueryCriteria extends BaseQueryReq {
    /** 精确 */
    @Query
    private Integer   callTemplateId ;
    @Query(type = Query.Type.INNER_LIKE)
    private String name;
    /** 精确 */
    @Query
    private Integer   type ;
}