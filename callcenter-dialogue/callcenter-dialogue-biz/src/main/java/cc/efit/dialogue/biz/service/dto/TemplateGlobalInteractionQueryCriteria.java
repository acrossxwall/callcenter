package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-21
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateGlobalInteractionQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   callTemplateId ;
}