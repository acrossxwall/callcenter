package cc.efit.call.api.repository;

import cc.efit.call.api.vo.line.LineInfo;
import cc.efit.call.api.domain.Line;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 中继线路网关表Repository接口
 * 
 * @author across
 * @date 2025-08-27
 */
public interface LineRepository extends LogicDeletedRepository<Line, Integer>, JpaSpecificationExecutor<Line> {

    Line findByLineName(String lineName);

    @Query("""
            select new cc.efit.call.api.vo.line.LineInfo(dl.id,dl.lineName,dla.concurrency)
            from Line dl join LineAssign dla on dl.id = dla.lineId
            """)
    List<LineInfo> selectAssignLineInfo();
}
