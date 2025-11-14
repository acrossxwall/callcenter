package cc.efit.dialogue.biz.rest;

import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.dialogue.biz.vo.node.NodeInfo;
import cc.efit.dialogue.biz.vo.node.TemplateNodeVo;
import cc.efit.exception.BadRequestException;
import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateFlow;
import cc.efit.dialogue.biz.service.TemplateFlowService;
import cc.efit.dialogue.biz.service.dto.TemplateFlowQueryCriteria;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cc.efit.annotation.Log;
import org.springframework.data.domain.Pageable;
import java.io.IOException;

/**
 * 流程节点Controller
 * 
 * @author across
 * @date 2025-08-14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/flow")
@Slf4j
public class TemplateFlowController {

    private final TemplateFlowService templateFlowService;

    /**
     * 查询流程节点列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateFlowQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateFlowService.queryAll(criteria,pageable));
    }

    /**
     * 导出流程节点列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("流程节点导出" )
    @PostMapping("/export")
    public void exportTemplateFlow(HttpServletResponse response, TemplateFlowQueryCriteria criteria) throws IOException {
        templateFlowService.download(templateFlowService.queryAll(criteria), response);
    }

    /**
     * 获取流程节点详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateFlowService.selectTemplateFlowById(id));
    }

    /**
     * 新增流程节点
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("流程节点新增" )
    @PostMapping
    public R add(@RequestBody TemplateFlow templateFlow) {
        if (templateFlow.getCallTemplateId()==null) {
            throw new BadRequestException("流程节点新增失败，模板id不能为空");
        }
        return R.ok(templateFlowService.insertTemplateFlow(templateFlow));
    }

    /**
     * 修改流程节点
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("流程节点修改" )
    @PostMapping(value = "/update" )
    public R edit(@RequestBody TemplateNodeInfo templateFlow) {
        NodeInfo nodeInfo = templateFlowService.updateTemplateFlow(templateFlow);
        return R.ok(nodeInfo);
    }

    /**
     * 删除流程节点
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("流程节点删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateFlowService.deleteTemplateFlowById(id);
        return R.ok();
    }

    /**
     * 查询模板下所有的非跳转节点
     * @param callTemplateId
     * @return
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping("/all/{id}")
    public R findTemplateFlowByCallTemplateId(@PathVariable("id") Integer callTemplateId) {
        return R.ok(templateFlowService.findTemplateFlowByCallTemplateId(callTemplateId));
    }
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("流程节点删除" )
    @PostMapping("/deleteNodeAndEdge")
    public R deleteFlowNodesAndEdges(@RequestBody TemplateNodeVo templateNodeInfo) {
        if (CollectionUtils.isEmpty(templateNodeInfo.nodes()) && CollectionUtils.isEmpty(templateNodeInfo.edges())) {
            throw new BadRequestException("流程节点删除失败，参数不正确");
        }
        log.info("收到删除流程节点的相关信息:{}", templateNodeInfo);
        templateFlowService.deleteFlowNodesAndEdges(templateNodeInfo);
        return R.ok();
    }
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping("/allLabels/{id}")
    public R findTemplateFlowLabels(@PathVariable("id") Integer callTemplateId) {
        return R.ok(templateFlowService.findTemplateFlowLabels(callTemplateId));
    }
}
