package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * tts全局设置对象 efit_template_global_tts
 * 
 * @author across
 * @date 2025-08-21
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_global_tts")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateGlobalTts extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String pitch;
}
