package cc.efit.db.base;

import cc.efit.db.annotation.Query;
import lombok.Data;
/**
 * 可在这里做基础字段添加
 * @author across
 * @date 2025-08-02 10:31
 * @return
 */
@Data
public class BaseQueryReq {
    @Query
    private Integer orgId;
}
