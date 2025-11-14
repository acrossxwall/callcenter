package cc.efit.system.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 多租户对象 sys_org
 * 
 * @author across
 * @date 2025-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOrgDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 企业名称 */
    private String name;

    /** 状态 1-启用 0-禁用 */
    private Integer status;
    /** 租户套餐id */
    private Integer packageId;
}
