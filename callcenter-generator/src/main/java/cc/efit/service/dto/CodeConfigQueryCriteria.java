package cc.efit.service.dto;

import cc.efit.db.annotation.Query;
import cc.efit.db.base.BaseQueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
public class CodeConfigQueryCriteria extends BaseQueryReq {

    @Query(type = Query.Type.INNER_LIKE)
    private String tableName;
    @Query(type = Query.Type.INNER_LIKE)
    private String tableComment;

    @Schema(name = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
