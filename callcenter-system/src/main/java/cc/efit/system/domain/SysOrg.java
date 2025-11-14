package cc.efit.system.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.db.base.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 多租户对象 efit_sys_org
 * 
 * @author across
 * @date 2025-08-06
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_sys_org")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class SysOrg extends  BaseEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 企业名称 */
    private String name;
    /** 状态 1-启用 0-禁用 */
    private Integer status;

    /** 租户套餐id */
    private Integer packageId;
}
