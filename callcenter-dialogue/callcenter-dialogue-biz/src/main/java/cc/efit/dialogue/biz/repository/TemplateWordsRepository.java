package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateWords;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 关键词库Repository接口
 * 
 * @author across
 * @date 2025-08-19
 */
public interface TemplateWordsRepository extends LogicDeletedRepository<TemplateWords, Integer>, JpaSpecificationExecutor<TemplateWords> {

    List<TemplateWords> findByCategoryIdIn(List<Integer> categoryIds);
}
