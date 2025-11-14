package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateKnowledge;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 知识库Repository接口
 * 
 * @author across
 * @date 2025-08-16
 */
public interface TemplateKnowledgeRepository extends LogicDeletedRepository<TemplateKnowledge, Integer>, JpaSpecificationExecutor<TemplateKnowledge> {

    TemplateKnowledge findByNameAndCallTemplateId(String name, Integer callTemplateId);

    List<TemplateKnowledge> findByCallTemplateId(Integer callTemplateId);
}
