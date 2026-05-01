package cc.efit.call.api.repository;

import cc.efit.call.api.domain.CallSystemStatistics;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 呼叫任务系统维度统计表Repository接口
 * 
 * @author across
 * @date 2025-10-22
 */
public interface CallSystemStatisticsRepository extends LogicDeletedRepository<CallSystemStatistics, Integer>, JpaSpecificationExecutor<CallSystemStatistics> {

    List<CallSystemStatistics> findByCallDateBetween(LocalDate start, LocalDate end);

    /**
     * 系统统计按机构、部门、用户、日期、时段幂等更新
     */
    Optional<CallSystemStatistics> findFirstByOrgIdAndDeptIdAndUserIdAndCallDateAndCallTimeOrderByIdDesc(
            Integer orgId, Integer deptId, Integer userId, LocalDate callDate, String callTime);
}
