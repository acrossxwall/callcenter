package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.api.vo.template.TemplateInfo;
import cc.efit.dialogue.biz.domain.CallTemplate;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * ai拨打话术Repository接口
 * 
 * @author across
 * @date 2025-08-09
 */
public interface CallTemplateRepository extends LogicDeletedRepository<CallTemplate, Integer>, JpaSpecificationExecutor<CallTemplate> {

    CallTemplate findByName(String name);
    @Query("""
            select new cc.efit.dialogue.api.vo.template.TemplateInfo(t.id,t.name)
             from CallTemplate t where t.status = 3
            """)
    List<TemplateInfo> selectReviewPassTemplate();
    @Query("update CallTemplate t set t.status = ?2 where t.id = ?1")
    @Modifying
    void updateTemplateStatusById(Integer id, Integer status);

    @Query("""
            select new cc.efit.dialogue.api.vo.template.TemplateInfo(t.id,t.name)
             from CallTemplate t where t.status = 3 and t.id = ?1
            """)
    TemplateInfo  selectReviewPassTemplateById(Integer id);
}
