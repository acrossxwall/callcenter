package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateGlobalInteraction;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 交互全局设置Repository接口
 * 
 * @author across
 * @date 2025-08-21
 */
public interface TemplateGlobalInteractionRepository extends LogicDeletedRepository<TemplateGlobalInteraction, Integer>, JpaSpecificationExecutor<TemplateGlobalInteraction> {

    TemplateGlobalInteraction findByCallTemplateId(Integer callTemplateId);

}
