package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateWords;
import cc.efit.dialogue.biz.service.TemplateWordsService;
import cc.efit.dialogue.biz.service.dto.TemplateWordsQueryCriteria;

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
 * 关键词库Controller
 * 
 * @author across
 * @date 2025-08-19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/words")
public class TemplateWordsController {

    private final TemplateWordsService templateWordsService;

    /**
     * 查询关键词库列表
     */
    @PreAuthorize("@cc.check('dialogue:words:list')")
    @GetMapping("/list")
    public R list(TemplateWordsQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateWordsService.queryAll(criteria,pageable));
    }

    /**
     * 导出关键词库列表
     */
    @PreAuthorize("@cc.check('dialogue:words:export')")
    @Log("关键词库导出" )
    @PostMapping("/export")
    public void exportTemplateWords(HttpServletResponse response, TemplateWordsQueryCriteria criteria) throws IOException {
        templateWordsService.download(templateWordsService.queryAll(criteria), response);
    }

    /**
     * 获取关键词库详细信息
     */
    @PreAuthorize("@cc.check('dialogue:words:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateWordsService.selectTemplateWordsById(id));
    }

    /**
     * 新增关键词库
     */
    @PreAuthorize("@cc.check('dialogue:words:add')")
    @Log("关键词库新增" )
    @PostMapping
    public R add(@RequestBody TemplateWords templateWords) {
        templateWordsService.insertTemplateWords(templateWords);
        return R.ok();
    }

    /**
     * 修改关键词库
     */
    @PreAuthorize("@cc.check('dialogue:words:edit')")
    @Log("关键词库修改" )
    @PutMapping
    public R edit(@RequestBody TemplateWords templateWords) {
        templateWordsService.updateTemplateWords(templateWords);
        return R.ok();
    }

    /**
     * 删除关键词库
     */
    @PreAuthorize("@cc.check('dialogue:words:remove')")
    @Log("关键词库删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateWordsService.deleteTemplateWordsById(id);
        return R.ok();
    }


    /**
     * 查询关键词库列表
     */
    @PreAuthorize("@cc.check('dialogue:words:list', 'dialogue:template:add', 'dialogue:template:edit')")
    @PostMapping("/listByCategoryIds")
    public R listByIds(@RequestBody TemplateWordsQueryCriteria criteria ) {
        return R.ok(templateWordsService.selectAllByCategoryIds(criteria.getCategoryIds()));
    }
}
