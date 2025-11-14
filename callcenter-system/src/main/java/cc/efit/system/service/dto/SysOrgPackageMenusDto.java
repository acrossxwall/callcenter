package cc.efit.system.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.Date;
/**
 * 租户套餐菜单关联表对象 efit_sys_org_package_menus
 * 
 * @author across
 * @date 2025-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOrgPackageMenusDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 菜单id */
    private Integer menuId;

    /** 租户套餐id */
    private Integer packageId;

}
