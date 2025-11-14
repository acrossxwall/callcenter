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

import cc.efit.data.permission.DataPermissionIgnore;
import cc.efit.org.permission.OrgPermissionIgnore;
import cc.efit.res.R;
import cc.efit.web.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.annotation.Log;
import cc.efit.modules.system.domain.Menu;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.system.service.MenuService;
import cc.efit.modules.system.service.dto.MenuDto;
import cc.efit.modules.system.service.dto.MenuQueryCriteria;
import cc.efit.modules.system.service.mapstruct.MenuMapper;
import cc.efit.utils.PageUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @date 2018-12-03
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：菜单管理")
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;
    private final MenuMapper menuMapper;
    private static final String ENTITY_NAME = "menu";

    @Operation(summary="导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('system:menu:list')")
    public void exportMenu(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {
        menuService.download(menuService.queryAll(criteria, false), response);
    }

    @GetMapping(value = "/build")
    @Operation(summary="获取前端所需菜单")
    @OrgPermissionIgnore
    @DataPermissionIgnore
    public R buildMenus(){
        List<MenuDto> menuDtoList = menuService.findByUser(SecurityUtils.getCurrentUserId());
        List<MenuDto> menus = menuService.buildTree(menuDtoList);
        return R.ok(menuService.buildMenus(menus) );
    }

    @Operation(summary="返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@cc.check('system:menu:list','system:roles:list')")
    public R queryAllMenu(@RequestParam Integer pid){
        return R.ok(menuService.getMenus(pid));
    }

    @Operation(summary="根据菜单ID返回所有子节点ID，包含自身ID")
    @GetMapping(value = "/child")
    @PreAuthorize("@cc.check('system:menu:list','system:roles:list')")
    public R childMenu(@RequestParam Integer id){
        Set<Menu> menuSet = new HashSet<>();
        List<MenuDto> menuList = menuService.getMenus(id);
        menuSet.add(menuService.findOne(id));
        menuSet = menuService.getChildMenus(menuMapper.toEntity(menuList), menuSet);
        Set<Integer> ids = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        return R.ok(ids);
    }

    @GetMapping
    @Operation(summary="查询菜单")
    @PreAuthorize("@cc.check('system:menu:list')")
    public R queryMenu(MenuQueryCriteria criteria) throws Exception {
        List<MenuDto> menuDtoList = menuService.queryAll(criteria, true);
        return R.ok(PageUtil.toPage(menuDtoList, menuDtoList.size()));
    }

    @Operation(summary="查询菜单:根据ID获取同级与上级数据")
    @GetMapping("/treeselect")
    @PreAuthorize("@cc.check('system:menu:list', 'system:roles:add', 'system:roles:edit')")
    public R getMenuTreeSelect() {
        Integer userType = SecurityUtils.getCurrentUserType();
        Integer orgId = SecurityUtils.getCurrentUserOrgId();
        return R.ok(menuService.getMenuTreeSelect(userType,orgId));
    }

    @Log("新增菜单")
    @Operation(summary="新增菜单")
    @PostMapping
    @PreAuthorize("@cc.check('system:menu:add')")
    public R createMenu(@Validated @RequestBody Menu resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        menuService.create(resources);
        return R.ok();
    }

    @Log("修改菜单")
    @Operation(summary="修改菜单")
    @PutMapping
    @PreAuthorize("@cc.check('system:menu:edit')")
    public R updateMenu(@Validated(Menu.Update.class) @RequestBody Menu resources){
        menuService.update(resources);
        return R.ok();
    }

    @Log("查询菜单")
    @Operation(summary="查询菜单")
    @GetMapping("/{id}")
    @PreAuthorize("@cc.check('system:menu:edit')")
    public R getMenu(@PathVariable("id") Integer id){
        return R.ok(menuService.findById(id));
    }

    @Log("删除菜单")
    @Operation(summary="删除菜单")
    @DeleteMapping
    @PreAuthorize("@cc.check('system:menu:remove')")
    public R deleteMenu(@RequestBody Set<Integer> ids){
        Set<Menu> menuSet = new HashSet<>();
        for (Integer id : ids) {
            List<MenuDto> menuList = menuService.getMenus(id);
            menuSet.add(menuService.findOne(id));
            menuSet = menuService.getChildMenus(menuMapper.toEntity(menuList), menuSet);
        }
        menuService.delete(menuSet);
        return R.ok();
    }

    @Log("删除菜单")
    @Operation(summary="删除菜单")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@cc.check('system:menu:remove')")
    public R deleteMenuById(@PathVariable("id") Integer id){
        Set<Menu> menuSet = new HashSet<>();
        List<MenuDto> menuList = menuService.getMenus(id);
        menuSet.add(menuService.findOne(id));
        menuSet = menuService.getChildMenus(menuMapper.toEntity(menuList), menuSet);
        menuService.delete(menuSet);
        return R.ok();
    }
}
