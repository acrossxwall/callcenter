package cc.efit.dialogue.biz.rest;

import cc.efit.dialogue.biz.vo.verbal.TemplateVerbalVo;
import cc.efit.exception.BadRequestException;
import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateVerbal;
import cc.efit.dialogue.biz.service.TemplateVerbalService;
import cc.efit.dialogue.biz.service.dto.TemplateVerbalQueryCriteria;

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
 * 话术Controller
 * 
 * @author across
 * @date 2025-08-18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/verbal")
@Slf4j
public class TemplateVerbalController {

    private final TemplateVerbalService templateVerbalService;

    /**
     * 查询话术列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateVerbalQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateVerbalService.queryAll(criteria,pageable));
    }

    /**
     * 导出话术列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("话术导出" )
    @PostMapping("/export")
    public void exportTemplateVerbal(HttpServletResponse response, TemplateVerbalQueryCriteria criteria) throws IOException {
        templateVerbalService.download(templateVerbalService.queryAll(criteria), response);
    }

    /**
     * 获取话术详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateVerbalService.selectTemplateVerbalById(id));
    }

    /**
     * 新增话术
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("话术新增" )
    @PostMapping
    public R add(@RequestBody TemplateVerbal templateVerbal) {
        templateVerbalService.insertTemplateVerbal(templateVerbal);
        return R.ok();
    }

    /**
     * 修改话术
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("话术修改" )
    @PutMapping
    public R edit(@RequestBody TemplateVerbal templateVerbal) {
        templateVerbalService.updateTemplateVerbal(templateVerbal);
        return R.ok();
    }

    /**
     * 删除话术
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("话术删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateVerbalService.deleteTemplateVerbalById(id);
        return R.ok();
    }
    @PostMapping("/import")
    @Log("上传话术录音" )
    public R importTemplateVerbal(@RequestBody TemplateVerbalVo vo) {
        log.info("上传话术录音:{}",vo);
        if (vo.fileId()==null || vo.callTemplateId()==null) {
            throw new BadRequestException("参数错误");
        }
        templateVerbalService.handlerBatchImportTemplateVerbal(vo);
        return R.ok();
    }
}
