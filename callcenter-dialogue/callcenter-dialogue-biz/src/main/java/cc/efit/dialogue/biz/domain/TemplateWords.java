package cc.efit.dialogue.biz.domain;

import cc.efit.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 关键词库对象 efit_template_words
 * 
 * @author across
 * @date 2025-08-19
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_words")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateWords extends BaseEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
