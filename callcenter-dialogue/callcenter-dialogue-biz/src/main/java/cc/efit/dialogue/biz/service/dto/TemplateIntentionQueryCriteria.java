package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-14
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateIntentionQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   callTemplateId ;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String  name ;
}