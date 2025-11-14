package cc.efit.call.biz.repository;

import cc.efit.call.biz.domain.CallCustomerBatch;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 客户名单批次表Repository接口
 * 
 * @author across
 * @date 2025-09-10
 */
public interface CallCustomerBatchRepository extends LogicDeletedRepository<CallCustomerBatch, Integer>, JpaSpecificationExecutor<CallCustomerBatch> {

}
