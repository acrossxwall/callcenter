package cc.efit.dialogue.biz.rest;

import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.service.TemplateKnowledgeService;
import cc.efit.dialogue.biz.service.dto.TemplateKnowledgeQueryCriteria;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cc.efit.annotation.Log;
import org.springframework.data.domain.Pageable;
import java.io.IOException;

/**
 * 知识库Controller
 * 
 * @author across
 * @date 2025-08-16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/knowledge")
public class TemplateKnowledgeController {

    private final TemplateKnowledgeService templateKnowledgeService;

    /**
     * 查询知识库列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateKnowledgeQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateKnowledgeService.queryAll(criteria,pageable));
    }

    /**
     * 导出知识库列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("知识库导出" )
    @PostMapping("/export")
    public void exportTemplateKnowledge(HttpServletResponse response, TemplateKnowledgeQueryCriteria criteria) throws IOException {
        templateKnowledgeService.download(templateKnowledgeService.queryAll(criteria), response);
    }

    /**
     * 获取知识库详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateKnowledgeService.selectTemplateKnowledgeById(id));
    }

    /**
     * 新增知识库
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("知识库新增" )
    @PostMapping
    public R add(@RequestBody TemplateKnowledgeVo templateKnowledge) {
        templateKnowledgeService.insertTemplateKnowledge(templateKnowledge);
        return R.ok();
    }

    /**
     * 修改知识库
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("知识库修改" )
    @PutMapping
    public R edit(@RequestBody TemplateKnowledgeVo templateKnowledge) {
        templateKnowledgeService.updateTemplateKnowledge(templateKnowledge);
        return R.ok();
    }

    /**
     * 删除知识库
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("知识库删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateKnowledgeService.deleteTemplateKnowledgeById(id);
        return R.ok();
    }

    /**
     * 获取模板所有知识库信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/all/{id}")
    public R getTemplateInfo(@PathVariable("id") Integer callTemplateId) {
        return R.ok(templateKnowledgeService.selectTemplateKnowledgeInfoByCallTemplateId(callTemplateId));
    }
}
