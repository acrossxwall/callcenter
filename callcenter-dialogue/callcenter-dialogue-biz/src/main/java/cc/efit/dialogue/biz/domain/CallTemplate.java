package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * ai拨打话术对象 efit_call_template
 * 
 * @author across
 * @date 2025-08-09
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_call_template")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class CallTemplate extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 话术名称 */
    private String name;
    /** 话术描述 */
    private String description;
    /** 行业 */
    private String industry;
    /** 状态 */
    private Integer status;
}
