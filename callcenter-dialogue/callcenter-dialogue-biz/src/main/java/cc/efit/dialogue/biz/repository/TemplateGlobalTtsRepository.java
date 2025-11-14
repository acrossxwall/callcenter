package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateGlobalTts;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * tts全局设置Repository接口
 * 
 * @author across
 * @date 2025-08-21
 */
public interface TemplateGlobalTtsRepository extends LogicDeletedRepository<TemplateGlobalTts, Integer>, JpaSpecificationExecutor<TemplateGlobalTts> {

    TemplateGlobalTts findByCallTemplateId(Integer callTemplateId);
}
