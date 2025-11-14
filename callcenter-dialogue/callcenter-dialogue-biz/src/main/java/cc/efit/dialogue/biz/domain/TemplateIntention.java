package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 意图分支对象 efit_template_intention
 * 
 * @author across
 * @date 2025-08-14
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_intention")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateIntention extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 话术模板id */
    private Integer callTemplateId;
    /** 意图名称 */
    private String name;
    /** 关键词 */
    private String keywords;
    /** 排序 */
    private Integer sort;
    /** 0-普通意图 1-默认意图 2-知识库意图 */
    private Integer type;
    /** 意图分类属性 0 肯定  1 否定  2 拒绝  3 中性  4-其他 */
    private Integer classify;
}
