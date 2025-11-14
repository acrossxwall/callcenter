package cc.efit.call.api.repository;

import cc.efit.call.api.domain.CallRecord;
import cc.efit.call.api.vo.record.CallRecordStatsDTO;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 呼叫记录表Repository接口
 * 
 * @author across
 * @date 2025-09-26
 */
public interface CallRecordRepository extends LogicDeletedRepository<CallRecord, Integer>, JpaSpecificationExecutor<CallRecord> {

    @Query("""
            SELECT new cc.efit.call.api.vo.record.CallRecordStatsDTO(
            c.taskId,
            COUNT(1),
            SUM(CASE WHEN c.status = 1 THEN 1 ELSE 0 END),
            SUM(c.duration)
            )
            FROM CallRecord c
            WHERE  c.callTime  BETWEEN :startDate AND :endDate
            GROUP BY c.taskId
            """)
    List<CallRecordStatsDTO> findCallStatsByDateRange(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate" ) LocalDate endDate);
}
