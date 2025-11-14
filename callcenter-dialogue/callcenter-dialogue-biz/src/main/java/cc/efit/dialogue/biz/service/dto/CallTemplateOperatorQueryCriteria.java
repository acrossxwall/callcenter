package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CallTemplateOperatorQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer type ;

    /** 话术模板id */
    @Query
    private Integer callTemplateId;
}