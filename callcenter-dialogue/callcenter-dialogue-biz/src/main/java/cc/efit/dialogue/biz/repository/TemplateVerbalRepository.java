package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateVerbal;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 话术Repository接口
 * 
 * @author across
 * @date 2025-08-18
 */
public interface TemplateVerbalRepository extends LogicDeletedRepository<TemplateVerbal, Integer>, JpaSpecificationExecutor<TemplateVerbal> {

    List<TemplateVerbal> findByCallTemplateIdAndIdIn(Integer callTemplateId,List<Integer> ids);
    @Query("""
            update TemplateVerbal t set t.content = ?2 where t.id = ?1
            """)
    @Modifying
    void updateTemplateVerbalById(  Integer id, String verbal);
    @Query("""
            update TemplateVerbal t set t.recording=?3, t.content = ?2,status=1 where t.id = ?1
            """)
    @Modifying
    void updateTemplateVerbalFilePathById(  Integer id, String verbal,String recording);

    @Query("""
            update TemplateVerbal t set t.recording=?2,  status=1 where t.id = ?1
            """)
    @Modifying
    void updateTemplateVerbalRecordingById(  Integer id,  String recording);

}
