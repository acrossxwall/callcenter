package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateIntention;
import cc.efit.db.base.LogicDeletedRepository;
import cc.efit.dialogue.biz.vo.node.NodeIntention;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 意图分支Repository接口
 * 
 * @author across
 * @date 2025-08-14
 */
public interface TemplateIntentionRepository extends LogicDeletedRepository<TemplateIntention, Integer>, JpaSpecificationExecutor<TemplateIntention> {

    TemplateIntention findByNameAndCallTemplateId(String name,Integer callTemplateId);

    List<TemplateIntention> findByCallTemplateIdOrderBySortAsc(Integer callTemplateId);

    @Query("""
            select new cc.efit.dialogue.biz.vo.node.NodeIntention(tfb.id,t.name) from
             TemplateIntention t join TemplateFlowBranch tfb on t.id=tfb.intentionId
             where t.callTemplateId = ?1 and tfb.flowId = ?2 order by tfb.sort asc
            """)
    List<NodeIntention> selectNodeIntentionByCallTemplateIdAndFlowId(Integer callTemplateId,Integer flowId);

    TemplateIntention findByCallTemplateIdAndType(Integer callTemplateId, Integer type);

    TemplateIntention findByCallTemplateIdAndTypeAndName(Integer callTemplateId, Integer type,String name);
}
