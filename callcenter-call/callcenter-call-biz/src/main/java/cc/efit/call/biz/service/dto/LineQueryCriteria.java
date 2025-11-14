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
public class LineQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   id ;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String  realm ;
    /** 精确 */
    @Query
    private Integer   register ;
    /** 精确 */
    @Query
    private Integer   regStatus ;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String  lineName ;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String  callNumber ;
    /** 精确 */
    @Query
    private Integer   status ;
}