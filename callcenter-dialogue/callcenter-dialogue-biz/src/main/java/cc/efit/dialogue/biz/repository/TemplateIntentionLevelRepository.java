package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateIntentionLevel;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 意向等级Repository接口
 * 
 * @author across
 * @date 2025-08-13
 */
public interface TemplateIntentionLevelRepository extends LogicDeletedRepository<TemplateIntentionLevel, Integer>, JpaSpecificationExecutor<TemplateIntentionLevel> {

    List<TemplateIntentionLevel> findByCallTemplateIdOrderBySortAsc(Integer callTemplateId);
    @Query("""
            update TemplateIntentionLevel t set t.type = 1 where t.callTemplateId = ?1 and t.name=?2
            """)
    @Modifying
    void updateTemplateDefaultLevel(Integer callTemplateId, String level);
    @Query("""
            update TemplateIntentionLevel t set t.type = 0 where t.callTemplateId = ?1 and t.type=1
            """)
    @Modifying
    void updateTemplateCommonLevel(Integer callTemplateId);

    TemplateIntentionLevel findByNameAndCallTemplateId(String name, Integer callTemplateId);

    int countByCallTemplateId(Integer callTemplateId);
    @Query("""
            update TemplateIntentionLevel t set t.sort = t.sort - 1 where t.sort > ?1
            and t.sort <= ?2
            and t.callTemplateId = ?3
            """)
    @Modifying
    void updateTemplateIntentionLevelSortDown(int sourceSort, int targetSort, Integer callTemplateId);
    @Query("""
            update TemplateIntentionLevel t set t.sort = t.sort + 1 where t.sort >= ?2
            and t.sort < ?1
            and t.callTemplateId = ?3
            """)
    @Modifying
    void updateTemplateIntentionLevelSortUp(int sourceSort, int targetSort, Integer callTemplateId);
}
