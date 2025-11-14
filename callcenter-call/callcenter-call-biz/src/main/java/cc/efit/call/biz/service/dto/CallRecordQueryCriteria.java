package cc.efit.call.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-09-26
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CallRecordQueryCriteria extends BaseQueryReq {

    /** 精确 */
    @Query
    private Integer   taskId ;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String  taskName ;
    /** 精确 */
    @Query
    private Integer   callTemplateId ;
    /** 精确 */
    @Query
    private String   callId ;
    /** 精确 */
    @Query
    private Integer   lineId ;
    /** 精确 */
    @Query
    private String   callNumber ;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<LocalDateTime> callTime;
    /** 精确 */
    @Query
    private String   intentionLevel ;
    /** 精确 */
    @Query
    private String   intentionName ;
}