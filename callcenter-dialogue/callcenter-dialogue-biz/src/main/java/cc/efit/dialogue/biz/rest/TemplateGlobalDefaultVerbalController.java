package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateGlobalDefaultVerbal;
import cc.efit.dialogue.biz.service.TemplateGlobalDefaultVerbalService;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalDefaultVerbalQueryCriteria;

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
 * 兜底话术全局设置Controller
 * 
 * @author across
 * @date 2025-11-11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/default")
public class TemplateGlobalDefaultVerbalController {

    private final TemplateGlobalDefaultVerbalService templateGlobalDefaultVerbalService;

    /**
     * 查询兜底话术全局设置列表
     */
    @PreAuthorize("@cc.check('dialogue:default:list')")
    @GetMapping("/list")
    public R list(TemplateGlobalDefaultVerbalQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateGlobalDefaultVerbalService.queryAll(criteria,pageable));
    }

    /**
     * 导出兜底话术全局设置列表
     */
    @PreAuthorize("@cc.check('dialogue:default:export')")
    @Log("兜底话术全局设置导出" )
    @PostMapping("/export")
    public void exportTemplateGlobalDefaultVerbal(HttpServletResponse response, TemplateGlobalDefaultVerbalQueryCriteria criteria) throws IOException {
        templateGlobalDefaultVerbalService.download(templateGlobalDefaultVerbalService.queryAll(criteria), response);
    }

    /**
     * 获取兜底话术全局设置详细信息
     */
    @PreAuthorize("@cc.check('dialogue:default:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateGlobalDefaultVerbalService.selectTemplateGlobalDefaultVerbalById(id));
    }

    /**
     * 新增兜底话术全局设置
     */
    @PreAuthorize("@cc.check('dialogue:default:add')")
    @Log("兜底话术全局设置新增" )
    @PostMapping
    public R add(@RequestBody TemplateGlobalDefaultVerbal templateGlobalDefaultVerbal) {
        templateGlobalDefaultVerbalService.insertTemplateGlobalDefaultVerbal(templateGlobalDefaultVerbal);
        return R.ok();
    }

    /**
     * 修改兜底话术全局设置
     */
    @PreAuthorize("@cc.check('dialogue:default:edit')")
    @Log("兜底话术全局设置修改" )
    @PutMapping
    public R edit(@RequestBody TemplateGlobalDefaultVerbal templateGlobalDefaultVerbal) {
        templateGlobalDefaultVerbalService.updateTemplateGlobalDefaultVerbal(templateGlobalDefaultVerbal);
        return R.ok();
    }

    /**
     * 删除兜底话术全局设置
     */
    @PreAuthorize("@cc.check('dialogue:default:remove')")
    @Log("兜底话术全局设置删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateGlobalDefaultVerbalService.deleteTemplateGlobalDefaultVerbalById(id);
        return R.ok();
    }
}
