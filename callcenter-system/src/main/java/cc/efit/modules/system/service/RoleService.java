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
package cc.efit.modules.system.service;

import cc.efit.modules.security.service.dto.AuthorityDto;
import cc.efit.modules.security.service.dto.JwtUserDto;
import cc.efit.modules.system.domain.Role;
import cc.efit.modules.system.service.dto.*;
import cc.efit.utils.PageResult;
import org.springframework.data.domain.Pageable;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 
 * @date 2018-12-03
 */
public interface RoleService {

    /**
     * 查询全部数据
     * @return /
     */
    List<RoleDto> queryAll();

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    RoleDto findById(Integer id);

    /**
     * 创建
     * @param resources /
     */
    void create(Role resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Role resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Integer> ids);

    /**
     * 根据用户ID查询
     * @param userId 用户ID
     * @return /
     */
    List<RoleSmallDto> findByUsersId(Integer userId);

    /**
     * 根据角色查询角色级别
     * @param roles /
     * @return /
     */
    Integer findByRoles(Set<Role> roles);

    /**
     * 修改绑定的菜单
     * @param resources /
     * @param roleDTO /
     */
    void updateMenu(Role resources, RoleDto roleDTO);

    /**
     * 解绑菜单
     * @param id /
     */
    void untiedMenu(Integer id);

    /**
     * 待条件分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<RoleDto> queryAll(RoleQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    List<RoleDto> queryAll(RoleQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<RoleDto> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    void buildPermissions(UserDto user, JwtUserDto jwtUserDto);

    /**
     * 验证是否被用户关联
     * @param ids /
     */
    void verification(Set<Integer> ids);

    /**
     * 根据菜单Id查询
     * @param menuIds /
     * @return /
     */
    List<Role> findInMenuId(List<Integer> menuIds);

    void updateRoleStatus(Role resources);

    PageResult<UserInfoDto> queryAllUserByRoleId(UserQueryCriteria criteria, Pageable pageable);

    void insertAuthUsers(Integer roleId, Integer[] userIds,Integer orgId);

    /**
     * 菜单id的套餐修改
     * @param add       新增的
     * @param delete     删除的
     * @param orgIds     组织id
     */
    void clearOrgPackageCache(Set<Integer> add, Set<Integer> delete,Set<Integer> orgIds);

    void clearOrgPackageCache(Set<Integer> add, Set<Integer> delete, Integer  orgId );
}
