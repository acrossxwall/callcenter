package cc.efit.dialogue.biz.repository;

import cc.efit.dialogue.biz.domain.TemplateFlow;
import cc.efit.db.base.LogicDeletedRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 流程节点Repository接口
 * 
 * @author across
 * @date 2025-08-14
 */
public interface TemplateFlowRepository extends LogicDeletedRepository<TemplateFlow, Integer>, JpaSpecificationExecutor<TemplateFlow> {
    /**
     * 根据节点名称和模板id查询流程节点
     * @param nodeName
     * @return
     */
    TemplateFlow findByNodeNameAndCallTemplateId(String nodeName, Integer callTemplateId);

    /**
     * 查询模板下所有流程节点
     * @param callTemplateId
     * @return
     */
    List<TemplateFlow> findByCallTemplateId(Integer callTemplateId);

    int countByCallTemplateIdAndNodeName(Integer callTemplateId,String nodeName);
    @Query("update TemplateFlow set coordinate=?2 where id=?1 ")
    @Modifying
    void updateTemplateFlowCoordinate(Integer id, String coordinate);

    /**
     * 根据模板id和节点类型查询流程节点，不查询跳转节点
     * @param callTemplateId
     * @param nodeType
     * @return
     */
    List<TemplateFlow> findByCallTemplateIdAndNodeTypeNot(Integer callTemplateId, Integer nodeType);
}
