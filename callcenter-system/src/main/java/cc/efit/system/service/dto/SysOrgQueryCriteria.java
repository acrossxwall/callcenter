package cc.efit.system.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-08-06
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOrgQueryCriteria extends BaseQueryReq {
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String  name ;

    /** 精确 */
    @Query
    private Integer   status ;

}