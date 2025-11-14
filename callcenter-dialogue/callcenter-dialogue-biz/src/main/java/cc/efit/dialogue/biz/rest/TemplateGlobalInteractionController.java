package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateGlobalInteraction;
import cc.efit.dialogue.biz.service.TemplateGlobalInteractionService;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalInteractionQueryCriteria;

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
 * 交互全局设置Controller
 * 
 * @author across
 * @date 2025-08-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/interaction")
public class TemplateGlobalInteractionController {

    private final TemplateGlobalInteractionService templateGlobalInteractionService;

    /**
     * 查询交互全局设置列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateGlobalInteractionQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateGlobalInteractionService.queryAll(criteria,pageable));
    }

    /**
     * 获取交互全局设置详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateGlobalInteractionService.selectTemplateGlobalInteractionById(id));
    }

    /**
     * 新增交互全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("交互全局设置新增" )
    @PostMapping
    public R add(@RequestBody TemplateGlobalInteraction templateGlobalInteraction) {
        templateGlobalInteractionService.insertTemplateGlobalInteraction(templateGlobalInteraction);
        return R.ok();
    }

    /**
     * 修改交互全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("交互全局设置修改" )
    @PutMapping
    public R edit(@RequestBody TemplateGlobalInteraction templateGlobalInteraction) {
        templateGlobalInteractionService.updateTemplateGlobalInteraction(templateGlobalInteraction);
        return R.ok();
    }

    /**
     * 删除交互全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("交互全局设置删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateGlobalInteractionService.deleteTemplateGlobalInteractionById(id);
        return R.ok();
    }
}
