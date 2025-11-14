package cc.efit.call.api.repository;

import cc.efit.call.api.domain.CallSystemStatistics;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

/**
 * 呼叫任务系统维度统计表Repository接口
 * 
 * @author across
 * @date 2025-10-22
 */
public interface CallSystemStatisticsRepository extends LogicDeletedRepository<CallSystemStatistics, Integer>, JpaSpecificationExecutor<CallSystemStatistics> {

    List<CallSystemStatistics> findByCallDateBetween(LocalDate start, LocalDate end);
}
