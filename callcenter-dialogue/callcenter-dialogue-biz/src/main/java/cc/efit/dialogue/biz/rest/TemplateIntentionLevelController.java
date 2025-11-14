package cc.efit.dialogue.biz.rest;

import cc.efit.dialogue.biz.vo.intention.IntentionLevelSetInfo;
import cc.efit.dialogue.biz.vo.intention.IntentionLevelSetSortInfo;
import cc.efit.res.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.dialogue.biz.domain.TemplateIntentionLevel;
import cc.efit.dialogue.biz.service.TemplateIntentionLevelService;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionLevelQueryCriteria;

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
 * 意向等级Controller
 * 
 * @author across
 * @date 2025-08-13
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/intention/level")
@Slf4j
public class TemplateIntentionLevelController {

    private final TemplateIntentionLevelService templateIntentionLevelService;

    /**
     * 查询意向等级列表
     */
    @PreAuthorize("@cc.check('dialogue:template:list')")
    @GetMapping("/list")
    public R list(TemplateIntentionLevelQueryCriteria criteria, Pageable pageable) {
        return R.ok(templateIntentionLevelService.queryAll(criteria,pageable));
    }

    /**
     * 导出意向等级列表
     */
    @PreAuthorize("@cc.check('dialogue:template:export')")
    @Log("意向等级导出" )
    @PostMapping("/export")
    public void exportTemplateIntentionLevel(HttpServletResponse response, TemplateIntentionLevelQueryCriteria criteria) throws IOException {
        templateIntentionLevelService.download(templateIntentionLevelService.queryAll(criteria), response);
    }

    /**
     * 获取意向等级详细信息
     */
    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(templateIntentionLevelService.selectTemplateIntentionLevelById(id));
    }

    /**
     * 新增意向等级
     */
    @PreAuthorize("@cc.check('dialogue:template:add')")
    @Log("意向等级新增" )
    @PostMapping
    public R add(@RequestBody TemplateIntentionLevel templateIntentionLevel) {
        templateIntentionLevelService.insertTemplateIntentionLevel(templateIntentionLevel);
        return R.ok();
    }

    /**
     * 修改意向等级
     */
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("意向等级修改" )
    @PutMapping
    public R edit(@RequestBody TemplateIntentionLevel templateIntentionLevel) {
        templateIntentionLevelService.updateTemplateIntentionLevel(templateIntentionLevel);
        return R.ok();
    }

    /**
     * 删除意向等级
     */
    @PreAuthorize("@cc.check('dialogue:template:remove')")
    @Log("意向等级删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        templateIntentionLevelService.deleteTemplateIntentionLevelById(id);
        return R.ok();
    }

    @PreAuthorize("@cc.check('dialogue:template:query')")
    @GetMapping(value = "/template/{id}")
    public R getTemplateIntentionLevelListByCallTemplateId(@PathVariable("id") Integer id) {
        return R.ok(templateIntentionLevelService.findTemplateIntentionLevelListByCallTemplateId(id));
    }

    @PostMapping("/setDefaultLevel")
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("设置默认意向等级" )
    public R setTemplateDefaultIntentionByCallTemplateId(@RequestBody IntentionLevelSetInfo info) {
        log.info("设置默认意向等级:{}",info);
        templateIntentionLevelService.setTemplateDefaultIntentionByCallTemplateId(info.callTemplateId(),info.level());
        return R.ok();
    }

    @PostMapping("/setSort")
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    @Log("设置意向等级排序" )
    public R setTemplateIntentionBySort(@RequestBody IntentionLevelSetSortInfo info) {
        log.info("设置意向等级排序:{}",info);
        templateIntentionLevelService.setTemplateIntentionLevelSort(info );
        return R.ok();
    }

}
