package cc.efit.system.req;

import java.util.List;

public record SysOrgPackageReq (

      Integer id,
    /** 套餐名 */
      String name,
    /** 租户状态（1正常 0停用） */
      Integer status,
    /** 备注 */
      String remark,
    /** 菜单id集合 */
      List<Integer> menuIds
){}
