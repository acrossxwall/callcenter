package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateFlow;
import cc.efit.dialogue.biz.service.dto.TemplateFlowDto;
import cc.efit.dialogue.biz.service.dto.TemplateFlowQueryCriteria;
import cc.efit.dialogue.biz.vo.node.NodeInfo;
import cc.efit.dialogue.biz.vo.node.TemplateAllNodeInfo;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.dialogue.biz.vo.node.TemplateNodeVo;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 流程节点Service接口
 * 
 * @author across
 * @date 2025-08-14
 */
public interface TemplateFlowService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateFlowDto> queryAll(TemplateFlowQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateFlowDto>
    */
    List<TemplateFlowDto> queryAll(TemplateFlowQueryCriteria criteria);
    /**
     * 查询流程节点
     * 
     * @param id 流程节点主键
     * @return 流程节点
     */
    TemplateNodeInfo selectTemplateFlowById(Integer id);


    /**
     * 新增流程节点
     * 
     * @param templateFlow 流程节点
     */
    NodeInfo insertTemplateFlow(TemplateFlow templateFlow);

    /**
     * 修改流程节点
     * 
     * @param templateFlow 流程节点
     */
    NodeInfo updateTemplateFlow(TemplateNodeInfo templateFlow);

    /**
     * 批量删除流程节点
     * 
     * @param ids 需要删除的流程节点主键集合
     */
    void deleteTemplateFlowByIds(Integer[] ids);

    /**
     * 删除流程节点信息
     * 
     * @param id 流程节点主键
     */
    void deleteTemplateFlowById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateFlowDto> all, HttpServletResponse response) throws IOException;

    List<TemplateAllNodeInfo> findTemplateFlowByCallTemplateId(Integer callTemplateId);

    void deleteFlowNodesAndEdges(TemplateNodeVo templateNodeInfo);

    List<String> findTemplateFlowLabels(Integer callTemplateId);
}
