package cc.efit.dialogue.biz.rest;

import cc.efit.nlu.INluModelService;
import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateGlobalNlu;
import cc.efit.dialogue.biz.service.TemplateGlobalNluService;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNluQueryCriteria;

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
 * nlu全局设置Controller
 * 
 * @author across
 * @date 2025-11-10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/nlu")
public class TemplateGlobalNluController {

    private final TemplateGlobalNluService templateGlobalNluService;

    private final INluModelService nluModelService;

    /**
     * 查询nlu全局设置列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateGlobalNluQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateGlobalNluService.queryAll(criteria,pageable));
    }

    /**
     * 导出nlu全局设置列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("nlu全局设置导出" )
    @PostMapping("/export")
    public void exportTemplateGlobalNlu(HttpServletResponse response, TemplateGlobalNluQueryCriteria criteria) throws IOException {
        templateGlobalNluService.download(templateGlobalNluService.queryAll(criteria), response);
    }

    /**
     * 获取nlu全局设置详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateGlobalNluService.selectTemplateGlobalNluById(id));
    }

    /**
     * 新增nlu全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("nlu全局设置新增" )
    @PostMapping
    public R add(@RequestBody TemplateGlobalNlu templateGlobalNlu) {
        templateGlobalNluService.insertTemplateGlobalNlu(templateGlobalNlu);
        return R.ok();
    }

    /**
     * 修改nlu全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("nlu全局设置修改" )
    @PutMapping
    public R edit(@RequestBody TemplateGlobalNlu templateGlobalNlu) {
        templateGlobalNluService.updateTemplateGlobalNlu(templateGlobalNlu);
        return R.ok();
    }

    /**
     * 删除nlu全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("nlu全局设置删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateGlobalNluService.deleteTemplateGlobalNluById(id);
        return R.ok();
    }
    @GetMapping("/modelInfo")
    public R getAllNluModelInfo() {
        return R.ok(nluModelService.listModelInfo());
    }
}
