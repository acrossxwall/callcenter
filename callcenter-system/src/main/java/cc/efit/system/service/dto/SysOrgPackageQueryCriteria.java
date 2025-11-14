package cc.efit.system.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseQueryReq;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import cc.efit.db.annotation.Query;

/**
 * @author across
 * @date 2025-10-28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOrgPackageQueryCriteria extends BaseQueryReq {

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String  name ;
    /** 精确 */
    @Query
    private Integer   status ;
}