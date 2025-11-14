package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * nlu全局设置对象 efit_template_global_nlu
 * 
 * @author across
 * @date 2025-11-10
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_global_nlu")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateGlobalNlu extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 模板id */
    private Integer callTemplateId;
    /** nlu设置启用 */
    private Integer enableNlu;
    /** 模板id */
    private String modeId;
    /** 匹配阈值 */
    private double threshold;
}
