package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateFlowBranch;
import cc.efit.dialogue.biz.service.TemplateFlowBranchService;
import cc.efit.dialogue.biz.service.dto.TemplateFlowBranchQueryCriteria;

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
 * 节点意图分支Controller
 * 
 * @author across
 * @date 2025-08-15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/branch")
public class TemplateFlowBranchController {

    private final TemplateFlowBranchService templateFlowBranchService;

    /**
     * 查询节点意图分支列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateFlowBranchQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateFlowBranchService.queryAll(criteria,pageable));
    }

    /**
     * 导出节点意图分支列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("节点意图分支导出" )
    @PostMapping("/export")
    public void exportTemplateFlowBranch(HttpServletResponse response, TemplateFlowBranchQueryCriteria criteria) throws IOException {
        templateFlowBranchService.download(templateFlowBranchService.queryAll(criteria), response);
    }

    /**
     * 获取节点意图分支详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateFlowBranchService.selectTemplateFlowBranchById(id));
    }

    /**
     * 新增节点意图分支
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("节点意图分支新增" )
    @PostMapping
    public R add(@RequestBody TemplateFlowBranch templateFlowBranch) {
        templateFlowBranchService.insertTemplateFlowBranch(templateFlowBranch);
        return R.ok();
    }

    /**
     * 修改节点意图分支
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("节点意图分支修改" )
    @PutMapping
    public R edit(@RequestBody TemplateFlowBranch templateFlowBranch) {
        templateFlowBranchService.updateTemplateFlowBranch(templateFlowBranch);
        return R.ok();
    }

    /**
     * 删除节点意图分支
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("节点意图分支删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateFlowBranchService.deleteTemplateFlowBranchById(id);
        return R.ok();
    }
}
