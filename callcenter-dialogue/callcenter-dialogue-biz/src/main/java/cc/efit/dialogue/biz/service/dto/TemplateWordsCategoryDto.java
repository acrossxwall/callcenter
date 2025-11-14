package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 词库分类对象 efit_template_words_category
 * 
 * @author across
 * @date 2025-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateWordsCategoryDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 部门id */
    private Integer deptId;

    /** 用户id */
    private Integer userId;

    /** 分类名称 */
    private String name;

    /** 1-通用词库 2-行业词库 */
    private Integer type;

}
