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
package cc.efit.modules.system.rest;

import cc.efit.modules.system.domain.UserRole;
import cc.efit.modules.system.service.dto.UserQueryCriteria;
import cc.efit.res.R;
import cc.efit.web.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.annotation.Log;
import cc.efit.modules.system.domain.Role;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.system.service.RoleService;
import cc.efit.modules.system.service.dto.RoleDto;
import cc.efit.modules.system.service.dto.RoleQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 *
 * @date 2018-12-03
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：角色管理")
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    private static final String ENTITY_NAME = "role";

    @Operation(summary="获取单个role")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@cc.check('system:roles:list')")
    public R findRoleById(@PathVariable Integer id){
        return R.ok(roleService.findById(id));
    }

    @Operation(summary="导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('system:roles:list')")
    public void exportRole(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        roleService.download(roleService.queryAll(criteria), response);
    }

    @Operation(summary="返回全部的角色")
    @GetMapping(value = "/all")
    @PreAuthorize("@cc.check('system:roles:list','system:user:add','system:user:edit')")
    public R queryAllRole(){
        return R.ok(roleService.queryAll());
    }

    @Operation(summary="查询角色")
    @GetMapping
    @PreAuthorize("@cc.check('system:roles:list')")
    public R queryRole(RoleQueryCriteria criteria, Pageable pageable){
        return R.ok(roleService.queryAll(criteria,pageable));
    }

    @Log("新增角色")
    @Operation(summary="新增角色")
    @PostMapping
    @PreAuthorize("@cc.check('system:roles:add')")
    public R createRole(@Validated @RequestBody Role resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        roleService.create(resources);
        return R.ok();
    }

    @Log("修改角色")
    @Operation(summary="修改角色")
    @PutMapping
    @PreAuthorize("@cc.check('system:roles:edit')")
    public R updateRole(@Validated(Role.Update.class) @RequestBody Role resources){
//        getLevels(resources.getLevel());
        roleService.update(resources);
        return R.ok();
    }

    @Log("修改角色菜单")
    @Operation(summary="修改角色菜单")
    @PutMapping(value = "/menu")
    @PreAuthorize("@cc.check('system:roles:edit')")
    public R updateRoleMenu(@RequestBody Role resources){
        RoleDto role = roleService.findById(resources.getId());
        roleService.updateMenu(resources,role);
        return R.ok();
    }

    @Log("删除角色")
    @Operation(summary="删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("@cc.check('system:roles:remove')")
    public R deleteRole(@PathVariable Integer id){
        // 验证是否被用户关联
        Set<Integer> set = Set.of(id);
        roleService.verification(set);
        roleService.delete(set);
        return R.ok();
    }

    @Log("修改角色状态")
    @Operation(summary="修改角色状态")
    @PutMapping("/changeStatus")
    @PreAuthorize("@cc.check('system:roles:edit')")
    public R changeUserStatus(@RequestBody Role resources){
        roleService.updateRoleStatus(resources);
        return R.ok();
    }

    @Operation(summary="查询角色分配用户")
    @GetMapping("/authUser/allocatedList")
    @PreAuthorize("@cc.check('system:roles:list', 'system:roles:edit')")
    public R queryUserByRoleId(UserQueryCriteria criteria, Pageable pageable){
        return R.ok(roleService.queryAllUserByRoleId(criteria,pageable));
    }
    @Operation(summary="查询角色分配用户")
    @GetMapping("/authUser/unallocatedList")
    @PreAuthorize("@cc.check('system:roles:list', 'system:roles:edit')")
    public R queryUserUnAllocateByRoleId(UserQueryCriteria criteria, Pageable pageable){
        return R.ok(roleService.queryAllUserByRoleId(criteria,pageable));
    }

    /**
     * 批量选择用户授权
     */
    @PreAuthorize("@cc.check('system:roles:edit')")
    @Log( "角色管理授权" )
    @PutMapping("/authUser/selectAll")
    public R selectAuthUserAll(Integer roleId, Integer[] userIds) {
        Integer orgId = SecurityUtils.getCurrentUserOrgId();
        roleService.insertAuthUsers(roleId, userIds,orgId);
        return R.ok();
    }
    @PreAuthorize("@cc.check('system:roles:remove')")
    @Log( "角色管理取消授权" )
    @PutMapping("/authUser/cancel")
    public R cleanAuthUserRole(@RequestBody UserRole userRole) {
        return R.ok();
    }

    @PreAuthorize("@cc.check('system:roles:edit')")
    @Log( "批量角色管理" )
    @PutMapping("/authUser/cancelAll")
    public R cancelAuthUserAll(Integer roleId, Integer[] userIds) {
        return R.ok();
    }
}
