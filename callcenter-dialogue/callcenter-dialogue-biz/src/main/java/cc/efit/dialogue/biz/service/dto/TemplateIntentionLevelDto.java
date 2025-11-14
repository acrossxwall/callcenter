package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 意向等级对象 efit_template_intention_level
 * 
 * @author across
 * @date 2025-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateIntentionLevelDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 意向等级 */
    private String name;

    /** 描述 */
    private String description;

    /** 类型 */
    private Integer type;

    /** 排序 */
    private Integer sort;

    /** 规则内容 */
    private String ruleContent;

}
