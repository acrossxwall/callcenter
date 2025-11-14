package cc.efit.dialogue.biz.service;

import cc.efit.dialogue.biz.domain.TemplateGlobalTts;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalTtsDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalTtsQueryCriteria;
import cc.efit.dialogue.biz.vo.global.TemplateGlobalSettingInfo;
import cc.efit.utils.PageResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
