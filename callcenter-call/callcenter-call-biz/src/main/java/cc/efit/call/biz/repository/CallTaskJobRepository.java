package cc.efit.call.biz.repository;

import cc.efit.call.biz.domain.CallTaskJob;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 外呼任务job表Repository接口
 * 
 * @author across
 * @date 2025-10-20
 */
public interface CallTaskJobRepository extends LogicDeletedRepository<CallTaskJob, Integer>, JpaSpecificationExecutor<CallTaskJob> {

    List<CallTaskJob> findByTaskId(Integer taskId);
}
