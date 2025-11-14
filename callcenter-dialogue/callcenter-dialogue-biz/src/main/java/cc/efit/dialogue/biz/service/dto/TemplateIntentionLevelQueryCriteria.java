package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-13
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateIntentionLevelQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private String   name ;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String  description ;

    /** 话术模板id */
    @Query
    private Integer callTemplateId;
}