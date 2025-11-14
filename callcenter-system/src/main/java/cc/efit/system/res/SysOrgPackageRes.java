package cc.efit.system.res;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.Set;

public record SysOrgPackageRes(
        Integer id,
        /** 套餐名 */
        String name,
        /** 租户状态（1正常 0停用） */
        Integer status,
        /** 备注 */
        String remark,
        /** 菜单id集合 */
        Set<Integer> menuIds,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        Timestamp createTime
) {
}
