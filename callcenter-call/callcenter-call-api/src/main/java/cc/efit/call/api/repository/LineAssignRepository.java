package cc.efit.call.api.repository;

import cc.efit.call.api.domain.LineAssign;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 线路分配表Repository接口
 * 
 * @author across
 * @date 2025-08-28
 */
public interface LineAssignRepository extends LogicDeletedRepository<LineAssign, Integer>, JpaSpecificationExecutor<LineAssign> {

    LineAssign findByLineIdAndAssignDeptId(Integer lineId, Integer assignDeptId);

    List<LineAssign> findByLineId(Integer lineId);

    @Query("""
        UPDATE LineAssign e SET e.deleted = 1 WHERE e.lineId=?1 and  e.id in ?2
        """
    )
    @Modifying
    void deleteByLineIdAndAssignDeptIdIn(Integer lineId, List<Integer> ids);
}
