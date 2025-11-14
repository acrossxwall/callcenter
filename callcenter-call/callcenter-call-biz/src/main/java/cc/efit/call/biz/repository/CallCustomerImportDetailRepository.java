package cc.efit.call.biz.repository;

import cc.efit.call.biz.domain.CallCustomerImportDetail;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 客户名单批次详细表Repository接口
 * 
 * @author across
 * @date 2025-09-12
 */
public interface CallCustomerImportDetailRepository extends LogicDeletedRepository<CallCustomerImportDetail, Integer>, JpaSpecificationExecutor<CallCustomerImportDetail> {

}
