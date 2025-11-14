package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 意向等级对象 efit_template_intention_level
 * 
 * @author across
 * @date 2025-08-13
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_intention_level")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateIntentionLevel extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 话术模板id */
    private Integer callTemplateId;
    /** 意向等级 */
    private String name;
    /** 描述 */
    private String description;
    /** 类型 1-默认 0-非默认 */
    private Integer type;
    /** 排序 */
    private Integer sort;
    /** 规则内容 */
    private String ruleContent;
}
