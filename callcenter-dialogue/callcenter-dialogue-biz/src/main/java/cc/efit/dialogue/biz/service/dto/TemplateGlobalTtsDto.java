package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * tts全局设置对象 efit_template_global_tts
 * 
 * @author across
 * @date 2025-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateGlobalTtsDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 话术模板id */
    private Integer callTemplateId;

    /** tts设置 */
    private Integer enableTts;

    /** 引擎 */
    private String engine;

    /** 音色 */
    private String voiceType;

    /** 语速 */
    private String speed;

    /** 音量 */
    private String volume;

    /** 音调 */
    private Integer pitch;

}
