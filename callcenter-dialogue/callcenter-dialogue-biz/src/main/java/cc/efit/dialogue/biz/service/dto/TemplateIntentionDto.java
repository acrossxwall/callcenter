package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 意图分支对象 efit_template_intention
 * 
 * @author across
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateIntentionDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 意图名称 */
    private String name;

    /** 关键词 */
    private String keywords;

    /** 排序 */
    private Integer sort;

    private Integer type;

    /** 意图分类属性 0 肯定  1 否定  2 拒绝  3 中性  4-其他 */
    private Integer classify;
}
