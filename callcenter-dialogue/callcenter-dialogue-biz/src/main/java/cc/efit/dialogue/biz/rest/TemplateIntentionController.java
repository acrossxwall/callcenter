package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateIntention;
import cc.efit.dialogue.biz.service.TemplateIntentionService;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionQueryCriteria;

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
 * 意图分支Controller
 * 
 * @author across
 * @date 2025-08-14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/intention")
public class TemplateIntentionController {

    private final TemplateIntentionService templateIntentionService;

    /**
     * 查询意图分支列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateIntentionQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateIntentionService.queryAll(criteria,pageable));
    }

    /**
     * 导出意图分支列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("意图分支导出" )
    @PostMapping("/export")
    public void exportTemplateIntention(HttpServletResponse response, TemplateIntentionQueryCriteria criteria) throws IOException {
        templateIntentionService.download(templateIntentionService.queryAll(criteria), response);
    }

    /**
     * 获取意图分支详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateIntentionService.selectTemplateIntentionById(id));
    }

    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/callTemplateId/{id}")
    public R getIntentionInfosByCallTemplateId(@PathVariable("id") Integer callTemplateId) {
        return R.ok(templateIntentionService.selectTemplateIntentionByCallTemplateId(callTemplateId));
    }

    /**
     * 新增意图分支
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("意图分支新增" )
    @PostMapping
    public R add(@RequestBody TemplateIntention templateIntention) {
        return R.ok(templateIntentionService.insertTemplateIntention(templateIntention));
    }

    /**
     * 修改意图分支
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("意图分支修改" )
    @PutMapping
    public R edit(@RequestBody TemplateIntention templateIntention) {
        templateIntentionService.updateTemplateIntention(templateIntention);
        return R.ok();
    }

    /**
     * 删除意图分支
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("意图分支删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateIntentionService.deleteTemplateIntentionById(id);
        return R.ok();
    }
}
