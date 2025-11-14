package cc.efit.system.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.Date;
/**
 * 租户套餐表对象 efit_sys_org_package
 * 
 * @author across
 * @date 2025-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOrgPackageDto extends BaseDTO implements Serializable {

    /** 套餐编号 */
    private Integer id;

    /** 套餐名 */
    private String name;

    /** 租户状态（1正常 0停用） */
    private Integer status;

    /** 备注 */
    private String remark;

}
