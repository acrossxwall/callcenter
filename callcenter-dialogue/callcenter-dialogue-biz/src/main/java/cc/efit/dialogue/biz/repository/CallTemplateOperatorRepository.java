package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.CallTemplateOperator;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 话术模板操作日志Repository接口
 * 
 * @author across
 * @date 2025-08-12
 */
public interface CallTemplateOperatorRepository extends LogicDeletedRepository<CallTemplateOperator, Integer>, JpaSpecificationExecutor<CallTemplateOperator> {

}
