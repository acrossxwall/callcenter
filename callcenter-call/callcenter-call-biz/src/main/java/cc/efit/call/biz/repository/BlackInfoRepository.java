package cc.efit.call.biz.repository;

import cc.efit.call.biz.domain.BlackInfo;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 黑名单库表Repository接口
 * 
 * @author across
 * @date 2025-08-27
 */
public interface BlackInfoRepository extends LogicDeletedRepository<BlackInfo, Integer>, JpaSpecificationExecutor<BlackInfo> {

}
