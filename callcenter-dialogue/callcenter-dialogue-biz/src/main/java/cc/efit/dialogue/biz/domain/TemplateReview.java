package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 话术审核记录对象 efit_template_review
 * 
 * @author across
 * @date 2025-08-26
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_review")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateReview extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 话术模板id */
    private Integer callTemplateId;
    /** 审核备注 */
    private String remark;
    /** 话术模板名称 */
    private String name;
    /** 2-审核中 3-审核通过 4-审核失败 */
    private Integer status;
    /** 审核时间 */
    private Timestamp checkTime;
}
