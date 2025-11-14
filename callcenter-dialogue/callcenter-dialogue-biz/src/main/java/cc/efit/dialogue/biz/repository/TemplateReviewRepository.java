package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateReview;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

/**
 * 话术审核记录Repository接口
 * 
 * @author across
 * @date 2025-08-26
 */
public interface TemplateReviewRepository extends LogicDeletedRepository<TemplateReview, Integer>, JpaSpecificationExecutor<TemplateReview> {

    @Query("""
            update TemplateReview set status = ?2, remark = ?3,checkTime=?4 where callTemplateId = ?1
            and status=2
            """)
    @Modifying
    void reviewTemplateByCallTemplateId(Integer callTemplateId, Integer status, String remark, LocalDateTime reviewTime);
}
