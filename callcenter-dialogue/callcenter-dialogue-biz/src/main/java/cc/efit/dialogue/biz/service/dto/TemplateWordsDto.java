package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 关键词库对象 efit_template_words
 * 
 * @author across
 * @date 2025-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateWordsDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 部门id */
    private Integer deptId;

    /** 用户id */
    private Integer userId;

    /** 词库 */
    private String name;

    /** 分类id */
    private Integer categoryId;

}
