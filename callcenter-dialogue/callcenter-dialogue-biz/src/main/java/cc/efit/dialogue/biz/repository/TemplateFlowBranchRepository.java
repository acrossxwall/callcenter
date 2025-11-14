package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;
import cc.efit.dialogue.biz.domain.TemplateFlowBranch;
import cc.efit.db.base.LogicDeletedRepository;
import cc.efit.dialogue.biz.vo.node.EdgeInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 节点意图分支Repository接口
 * 
 * @author across
 * @date 2025-08-15
 */
public interface TemplateFlowBranchRepository extends LogicDeletedRepository<TemplateFlowBranch, Integer>, JpaSpecificationExecutor<TemplateFlowBranch> {

    @Query("UPDATE TemplateFlowBranch e SET e.deleted = 1 WHERE e.callTemplateId = ?1 and e.flowId = ?2 and e.intentionId = ?3")
    @Modifying
    void logicDeleteByCallTemplateIdAndFlowIdAndIntentionId(Integer callTemplateId,Integer flowId,Integer intentionId);

    @Query("UPDATE TemplateFlowBranch e SET e.deleted = 1 WHERE e.callTemplateId = ?1 and e.flowId = ?2 ")
    @Modifying
    void logicDeleteByCallTemplateIdAndFlowId(Integer callTemplateId,Integer flowId);


    @Query("select e.intentionId from  TemplateFlowBranch e where e.callTemplateId = ?1 and e.flowId = ?2 ")
    List<Integer> selectIntentionIdFindByCallTemplateIdAndFlowId(Integer callTemplateId, Integer flowId);
    @Query("""
            update TemplateFlowBranch e set e.targetFlowId = ?1,e.edgeInfo=?2
            where e.callTemplateId = ?3 and e.flowId = ?4 and e.id = ?5
            """)
    @Modifying
    void updateFlowBranchInfo(Integer targetFlowId, EdgeInfo edgeInfo, Integer callTemplateId, Integer flowId, Integer id);

    List<TemplateFlowBranch> findByCallTemplateId(Integer callTemplateId);

    @Query("UPDATE TemplateFlowBranch e SET e.targetFlowId = null,e.edgeInfo = null WHERE e.callTemplateId = ?1 and e.targetFlowId = ?2 ")
    @Modifying
    void logicDeleteByCallTemplateIdAndTargetFlowId(Integer callTemplateId, Integer id);
    @Query("UPDATE TemplateFlowBranch e SET e.targetFlowId = null,e.edgeInfo = null WHERE e.callTemplateId = ?1 and e.id = ?2 ")
    @Modifying
    void logicDeleteByCallTemplateIdAndId(Integer callTemplateId, Integer branchId);

    @Query("""
        SELECT new cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo( e.flowId,
        e.intentionId,e.targetFlowId,ti.type,ti.name,ti.classify ) FROM TemplateFlowBranch e join TemplateIntention ti on
        e.intentionId = ti.id   WHERE e.callTemplateId = ?1
        """)
    List<TemplateNodeBranchInfo> selectNodeBranchInfoByCallTemplateId (Integer callTemplateId );
}
