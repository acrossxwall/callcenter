package cc.efit.dialogue.biz.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateGlobalNoreply;
import cc.efit.dialogue.biz.service.TemplateGlobalNoreplyService;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNoreplyQueryCriteria;

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
 * 无应答全局设置Controller
 * 
 * @author across
 * @date 2025-08-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/noreply")
public class TemplateGlobalNoreplyController {

    private final TemplateGlobalNoreplyService templateGlobalNoreplyService;

    /**
     * 查询无应答全局设置列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateGlobalNoreplyQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateGlobalNoreplyService.queryAll(criteria,pageable));
    }


    /**
     * 获取无应答全局设置详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateGlobalNoreplyService.selectTemplateGlobalNoreplyById(id));
    }

    /**
     * 新增无应答全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("无应答全局设置新增" )
    @PostMapping
    public R add(@RequestBody TemplateGlobalNoreply templateGlobalNoreply) {
        templateGlobalNoreplyService.insertTemplateGlobalNoreply(templateGlobalNoreply);
        return R.ok();
    }

    /**
     * 修改无应答全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("无应答全局设置修改" )
    @PutMapping
    public R edit(@RequestBody TemplateGlobalNoreply templateGlobalNoreply) {
        templateGlobalNoreplyService.updateTemplateGlobalNoreply(templateGlobalNoreply);
        return R.ok();
    }

    /**
     * 删除无应答全局设置
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("无应答全局设置删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateGlobalNoreplyService.deleteTemplateGlobalNoreplyById(id);
        return R.ok();
    }
}
