package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateWordsCategory;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 词库分类Repository接口
 * 
 * @author across
 * @date 2025-08-19
 */
public interface TemplateWordsCategoryRepository extends LogicDeletedRepository<TemplateWordsCategory, Integer>, JpaSpecificationExecutor<TemplateWordsCategory> {

}
