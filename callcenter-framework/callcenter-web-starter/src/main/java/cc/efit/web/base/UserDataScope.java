package cc.efit.web.base;

import java.util.Set;

public record UserDataScope(
        /**
         * 部门ID集合
         */
        Set<Integer> deptIds,
        /**
         * 是否所有数据
         */
        boolean all,
        /**
         * 是否仅自己，当self为true时，deptIds也有值
         * 说明一个用户两个角色，一个角色查看仅自己的数据，其他角色看部门的
         * 取并集 直接就不判断self
         * 如果 deptIds 是空代表，当前用户只能看自己创建的数据
         */
        boolean self
) {}