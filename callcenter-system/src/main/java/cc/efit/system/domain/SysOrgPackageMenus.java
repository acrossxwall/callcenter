package cc.efit.system.domain;

import cc.efit.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 租户套餐菜单关联表对象 efit_sys_org_package_menus
 * 
 * @author across
 * @date 2025-10-28
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_sys_org_package_menus")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class SysOrgPackageMenus extends BaseEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 菜单id */
    private Integer menuId;
    /** 租户套餐id */
    private Integer packageId;
}
