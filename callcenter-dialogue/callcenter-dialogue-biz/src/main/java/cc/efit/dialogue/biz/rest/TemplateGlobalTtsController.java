package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateGlobalTts;
import cc.efit.dialogue.biz.service.TemplateGlobalTtsService;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalTtsQueryCriteria;

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
 * tts全局设置Controller
 * 
 * @author across
 * @date 2025-08-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/tts")
public class TemplateGlobalTtsController {

    private final TemplateGlobalTtsService templateGlobalTtsService;

    /**
     * 查询tts全局设置列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateGlobalTtsQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateGlobalTtsService.queryAll(criteria,pageable));
    }


    /**
     * 获取tts全局设置详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateGlobalTtsService.selectTemplateGlobalTtsById(id));
    }

    /**
     * 新增tts全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("tts全局设置新增" )
    @PostMapping
    public R add(@RequestBody TemplateGlobalTts templateGlobalTts) {
        templateGlobalTtsService.insertTemplateGlobalTts(templateGlobalTts);
        return R.ok();
    }

    /**
     * 修改tts全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("tts全局设置修改" )
    @PutMapping
    public R edit(@RequestBody TemplateGlobalTts templateGlobalTts) {
        templateGlobalTtsService.updateTemplateGlobalTts(templateGlobalTts);
        return R.ok();
    }

    /**
     * 删除tts全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("tts全局设置删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateGlobalTtsService.deleteTemplateGlobalTtsById(id);
        return R.ok();
    }
}
