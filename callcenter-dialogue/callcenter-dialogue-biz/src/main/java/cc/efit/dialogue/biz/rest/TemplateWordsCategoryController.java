package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateWordsCategory;
import cc.efit.dialogue.biz.service.TemplateWordsCategoryService;
import cc.efit.dialogue.biz.service.dto.TemplateWordsCategoryQueryCriteria;

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
 * 词库分类Controller
 * 
 * @author across
 * @date 2025-08-19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/category")
public class TemplateWordsCategoryController {

    private final TemplateWordsCategoryService templateWordsCategoryService;

    /**
     * 查询词库分类列表
     */
    @PreAuthorize("@cc.check('dialogue:category:list')")
    @GetMapping("/list")
    public R list(TemplateWordsCategoryQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateWordsCategoryService.queryAll(criteria,pageable));
    }

    /**
     * 导出词库分类列表
     */
    @PreAuthorize("@cc.check('dialogue:category:export')")
    @Log("词库分类导出" )
    @PostMapping("/export")
    public void exportTemplateWordsCategory(HttpServletResponse response, TemplateWordsCategoryQueryCriteria criteria) throws IOException {
        templateWordsCategoryService.download(templateWordsCategoryService.queryAll(criteria), response);
    }

    /**
     * 获取词库分类详细信息
     */
    @PreAuthorize("@cc.check('dialogue:category:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateWordsCategoryService.selectTemplateWordsCategoryById(id));
    }

    /**
     * 新增词库分类
     */
    @PreAuthorize("@cc.check('dialogue:category:add')")
    @Log("词库分类新增" )
    @PostMapping
    public R add(@RequestBody TemplateWordsCategory templateWordsCategory) {
        templateWordsCategoryService.insertTemplateWordsCategory(templateWordsCategory);
        return R.ok();
    }

    /**
     * 修改词库分类
     */
    @PreAuthorize("@cc.check('dialogue:category:edit')")
    @Log("词库分类修改" )
    @PutMapping
    public R edit(@RequestBody TemplateWordsCategory templateWordsCategory) {
        templateWordsCategoryService.updateTemplateWordsCategory(templateWordsCategory);
        return R.ok();
    }

    /**
     * 删除词库分类
     */
    @PreAuthorize("@cc.check('dialogue:category:remove')")
    @Log("词库分类删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateWordsCategoryService.deleteTemplateWordsCategoryById(id);
        return R.ok();
    }

    @GetMapping("/all")
    @PreAuthorize("@cc.check('dialogue:category:query')")
    public R getAllTemplateWordsCategory() {
        return R.ok(templateWordsCategoryService.getAllCategoryInfo());
    }
}
