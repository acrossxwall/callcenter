package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateGlobalNoreply;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 无应答全局设置Repository接口
 * 
 * @author across
 * @date 2025-08-21
 */
public interface TemplateGlobalNoreplyRepository extends LogicDeletedRepository<TemplateGlobalNoreply, Integer>, JpaSpecificationExecutor<TemplateGlobalNoreply> {
    TemplateGlobalNoreply findByCallTemplateId(Integer callTemplateId);
}
