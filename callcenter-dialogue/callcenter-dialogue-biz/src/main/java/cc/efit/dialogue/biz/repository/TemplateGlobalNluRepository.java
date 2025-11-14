package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateGlobalNlu;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * nlu全局设置Repository接口
 * 
 * @author across
 * @date 2025-11-10
 */
public interface TemplateGlobalNluRepository extends LogicDeletedRepository<TemplateGlobalNlu, Integer>, JpaSpecificationExecutor<TemplateGlobalNlu> {

    TemplateGlobalNlu findByCallTemplateId(Integer callTemplateId);
}
