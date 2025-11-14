package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateGlobalDefaultVerbal;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 兜底话术全局设置Repository接口
 * 
 * @author across
 * @date 2025-11-11
 */
public interface TemplateGlobalDefaultVerbalRepository extends LogicDeletedRepository<TemplateGlobalDefaultVerbal, Integer>, JpaSpecificationExecutor<TemplateGlobalDefaultVerbal> {

    TemplateGlobalDefaultVerbal findByCallTemplateId(Integer callTemplateId);
}
