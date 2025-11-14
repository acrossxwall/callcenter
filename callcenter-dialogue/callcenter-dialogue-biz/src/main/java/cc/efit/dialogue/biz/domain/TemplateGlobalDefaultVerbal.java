package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 兜底话术全局设置对象 efit_template_global_default_verbal
 * 
 * @author across
 * @date 2025-11-11
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_global_default_verbal")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateGlobalDefaultVerbal extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 模板id */
    private Integer callTemplateId;
    /** 兜底话术设置启用 */
    private Integer enableDefault;
    /** 触发动作 1-挂机 0-跳转节点 */
    private Integer defaultAction;
    /** 目标节点id */
    private Integer targetFlowId;
    /** 话术id */
    private Integer verbalId;
}
