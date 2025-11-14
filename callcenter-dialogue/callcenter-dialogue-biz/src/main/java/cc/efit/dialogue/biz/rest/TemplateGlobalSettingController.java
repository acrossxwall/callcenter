package cc.efit.dialogue.biz.rest;

import cc.efit.annotation.Log;
import cc.efit.dialogue.biz.service.TemplateGlobalSettingsService;
import cc.efit.dialogue.biz.vo.global.*;
import cc.efit.res.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dialogue/global/settings")
@Slf4j
public class TemplateGlobalSettingController {

    private final TemplateGlobalSettingsService settingsService;

    @GetMapping("/{id}")
    @PreAuthorize("@cc.check('dialogue:template:list')")
    public R getTemplateGlobalSettings(@PathVariable("id") Integer id) {
        return R.ok(settingsService.getTemplateGlobalSettings(id));
    }

    @PostMapping
    @Log("保存话术模板全局设置")
    @PreAuthorize("@cc.check('dialogue:template:edit')")
    public R saveTemplateGlobalSettings(@RequestBody TemplateGlobalSettingInfo info) {
        log.info("收到保存模板全局配置信息:{}", info);
        settingsService.saveTemplateGlobalSettings(info);
        return R.ok();
    }


}
