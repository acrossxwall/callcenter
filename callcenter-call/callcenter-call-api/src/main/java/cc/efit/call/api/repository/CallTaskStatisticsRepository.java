package cc.efit.call.api.repository;

import cc.efit.call.api.domain.CallTaskStatistics;
import cc.efit.call.api.vo.task.SystemStatisticsInfo;
import cc.efit.call.api.vo.task.TaskStatisticsInfo;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * 呼叫任务统计表表Repository接口
 * 
 * @author across
 * @date 2025-10-15
 */
public interface CallTaskStatisticsRepository extends LogicDeletedRepository<CallTaskStatistics, Integer>, JpaSpecificationExecutor<CallTaskStatistics> {

    CallTaskStatistics findByTaskIdAndCallDate(Integer taskId, LocalDate callDate);
    @Query("""
            SELECT new cc.efit.call.api.vo.task.TaskStatisticsInfo(
            COUNT(1),
            SUM(c.totalCustomers),
            SUM(c.calledCustomers),
            SUM(c.connectCount)
            )
            FROM CallTaskStatistics c
            WHERE  c.callDate  BETWEEN ?1 AND ?2
            """)
    TaskStatisticsInfo selectTaskStatisticsInfo(LocalDate startDate, LocalDate endDate);
    @Query("""
            SELECT new cc.efit.call.api.vo.task.SystemStatisticsInfo(
            SUM(c.totalCustomers),
            SUM(c.calledCustomers),
            SUM(c.connectCount),
            SUM(c.duration),
            c.orgId,
            c.deptId,
            c.userId
            )
            FROM CallTaskStatistics c
            WHERE  c.callDate  BETWEEN ?1 AND ?2
            group by c.orgId,c.deptId,c.userId
            """)
    List<SystemStatisticsInfo> selectSystemStatisticsInfo(LocalDate startDate, LocalDate endDate);
}
