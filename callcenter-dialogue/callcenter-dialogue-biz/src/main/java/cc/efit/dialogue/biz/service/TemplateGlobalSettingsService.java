package cc.efit.dialogue.biz.service;

import cc.efit.dialogue.biz.vo.global.TemplateGlobalSettingInfo;

/**
 * 全局设置Service接口
 * 
 * @author across
 * @date 2025-08-21
 */
public interface TemplateGlobalSettingsService {

    void saveTemplateGlobalSettings(TemplateGlobalSettingInfo info);

    TemplateGlobalSettingInfo getTemplateGlobalSettings(Integer id);
}
