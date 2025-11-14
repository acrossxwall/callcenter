/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cc.efit.modules.system.service.impl;

import cc.efit.web.base.UserDataScope;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import cc.efit.modules.system.domain.Dept;
import cc.efit.modules.system.service.DataService;
import cc.efit.modules.system.service.DeptService;
import cc.efit.modules.system.service.RoleService;
import cc.efit.modules.system.service.dto.RoleSmallDto;
import cc.efit.modules.system.service.dto.UserDto;
import cc.efit.utils.CacheKey;
import cc.efit.redis.utils.RedisUtils;
import cc.efit.enums.DataScopeEnum;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @description 数据权限服务实现
 * @date 2020-05-07
 **/
@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final RedisUtils redisUtils;
    private final RoleService roleService;
    private final DeptService deptService;

    /**
     * 用户角色和用户部门改变时需清理缓存
     * @param user /
     * @return /
     */
    @Override
    public UserDataScope getDeptIds(UserDto user) {
        String key = CacheKey.DATA_USER + user.getId();
        List<Integer> ids = redisUtils.getList(key, Integer.class);
        boolean all = false;
        boolean self = false;
        if (CollUtil.isEmpty(ids)) {
            // 用于存储部门id
            Set<Integer> deptIds = new HashSet<>();
            // 查询用户角色
            List<RoleSmallDto> roleSet = roleService.findByUsersId(user.getId());
            // 获取对应的部门ID
            for (RoleSmallDto role : roleSet) {
                DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
                switch (Objects.requireNonNull(dataScopeEnum)) {
                    case ALL:
                        all = true;
                        break;
                    case THIS_USER:
                        self = true;
                        break;
                    case THIS_DEPT_LEVEL:
                        deptIds.add(user.getDept().getId());
                        break;
                    case CUSTOMIZE:
                        deptIds.addAll(getCustomize(deptIds, role));
                        break;
                    case THIS_AND_SUB_LEVEL:
                        deptIds.addAll( getSubDeptIds(user.getDept().getId()));
                        break;
                    default:
                        break;
                }
                if (all) {
                    //如果有一个角色是all,也就不用再循环加载其他角色了
                    break;
                }
            }
            ids = new ArrayList<>(deptIds);
            redisUtils.set(key, ids, 1, TimeUnit.DAYS);
        }
        Set<Integer> deptIds = new HashSet<>(ids);
        return new UserDataScope(all?null:deptIds, all, self);
    }

    private Set<Integer> getSubDeptIds(Integer id) {
        Set<Integer> deptIds = new HashSet<>();
        deptIds.add(id);
        List<Dept> depts = deptService.findByPid(id);
        if (CollUtil.isNotEmpty(depts)) {
            for (Dept dept : depts) {
                deptIds.add(dept.getId() );
                deptIds.addAll(getSubDeptIds(dept.getId()));
            }
        }
        return deptIds;
    }

    /**
     * 获取自定义的数据权限
     * @param deptIds 部门ID
     * @param role 角色
     * @return 数据权限ID
     */
    public Set<Integer> getCustomize(Set<Integer> deptIds, RoleSmallDto role){
        Set<Dept> depts = deptService.findByRoleId(role.getId());
        for (Dept dept : depts) {
            deptIds.add(dept.getId());
            List<Dept> deptChildren = deptService.findByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptChildren)) {
                deptIds.addAll(deptService.getDeptChildren(deptChildren));
            }
        }
        return deptIds;
    }
}
