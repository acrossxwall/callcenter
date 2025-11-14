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

import cc.efit.db.utils.QueryHelp;
import cc.efit.enums.CommonStatusEnum;
import cc.efit.core.enums.YesNoEnum;
import cc.efit.modules.system.service.dto.MenuTreeDto;
import cc.efit.redis.utils.RedisUtils;
import cc.efit.system.repository.SysOrgPackageMenusRepository;
import cc.efit.web.enums.UserTypeEnum;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import cc.efit.modules.system.domain.Menu;
import cc.efit.modules.system.domain.Role;
import cc.efit.modules.system.domain.User;
import cc.efit.modules.system.domain.vo.MenuMetaVo;
import cc.efit.modules.system.domain.vo.MenuVo;
import cc.efit.exception.BadRequestException;
import cc.efit.exception.EntityExistException;
import cc.efit.modules.system.repository.MenuRepository;
import cc.efit.modules.system.repository.UserRepository;
import cc.efit.modules.system.service.MenuService;
import cc.efit.modules.system.service.RoleService;
import cc.efit.modules.system.service.dto.MenuDto;
import cc.efit.modules.system.service.dto.MenuQueryCriteria;
import cc.efit.modules.system.service.dto.RoleSmallDto;
import cc.efit.modules.system.service.mapstruct.MenuMapper;
import cc.efit.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final MenuMapper menuMapper;
    private final RoleService roleService;
    private final RedisUtils redisUtils;
    private final SysOrgPackageMenusRepository orgPackageMenusRepository;

    private static final String HTTP_PRE = "http://";
    private static final String HTTPS_PRE = "https://";
    private static final String BAD_REQUEST = "外链必须以http://或者https://开头";
    
    @Override
    public List<MenuDto> queryAll(MenuQueryCriteria criteria, Boolean isQuery) throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "menuSort");
        criteria.setPidIsNull(null);
        return menuMapper.toDto(menuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),sort));
    }

    @Override
    public MenuDto findById(Integer id) {
        String key = CacheKey.MENU_ID + id;
        Menu menu = redisUtils.get(key, Menu.class);
        if(menu == null){
            menu = menuRepository.findById(id).orElseGet(Menu::new);
            ValidationUtil.isNull(menu.getId(),"Menu","id",id);
            redisUtils.set(key, menu, 1, TimeUnit.DAYS);
        }
        return menuMapper.toDto(menu);
    }

    /**
     * 用户角色改变时需清理缓存
     * @param currentUserId /
     * @return /
     */
    @Override
    public List<MenuDto> findByUser(Integer currentUserId) {
        String key = CacheKey.MENU_USER + currentUserId;
        List<Menu> menus = redisUtils.getList(key, Menu.class);
        if (CollUtil.isEmpty(menus)){
            List<RoleSmallDto> roles = roleService.findByUsersId(currentUserId);
            Set<Integer> roleIds = roles.stream().map(RoleSmallDto::getId).collect(Collectors.toSet());
            LinkedHashSet<Menu> data = menuRepository.findByRoleIdsAndTypeNot(roleIds, 2);
            menus = new ArrayList<>(data);
            redisUtils.set(key, menus, 1, TimeUnit.DAYS);
        }
        return menus.stream().map(menuMapper::toDto).toList();
    }

    @Override
    public List<MenuTreeDto> getMenuTreeSelect(Integer userType,Integer orgId) {
        UserTypeEnum type = UserTypeEnum.getUserTypeByCode(userType);
        List<Integer> packageMenuIds = null;
        List<Menu> menus = switch (type) {
            case SYSTEM_ADMINISTRATOR -> menuRepository.findByPidIsNullAndStatusOrderByMenuSort(CommonStatusEnum.ENABLE.getCode());
            case ORG_ADMINISTRATOR -> {
                packageMenuIds = orgPackageMenusRepository.selectMenuIdByOrgId(orgId);
                yield menuRepository.findByPidIsNullAndStatusAndIdInOrderByMenuSort(CommonStatusEnum.ENABLE.getCode(),packageMenuIds);
            }
            case NORMAL -> null;
        };
        if (menus == null) {
            return null;
        }
        Set<Integer> finalPackageMenuIds =  packageMenuIds==null?null:new HashSet<>(packageMenuIds);
        return menus.stream().map(s->toMenuTreeDto(s, finalPackageMenuIds)).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Menu resources) {
        if(menuRepository.findByTitle(resources.getTitle()) != null){
            throw new EntityExistException("菜单名称已存在");
        }
        if(StringUtils.isNotBlank(resources.getRouteName() )){
            if(menuRepository.findByRouteName(resources.getRouteName()) != null){
                throw new EntityExistException("组件名称已存在");
            }
        }
        if (Integer.valueOf(0).equals(resources.getPid())) {
            resources.setPid(null);
        }
        if(YesNoEnum.YES.getCode().equals(resources.getFrame())){
            if (!(resources.getPath().toLowerCase().startsWith(HTTP_PRE)||resources.getPath().toLowerCase().startsWith(HTTPS_PRE))) {
                throw new BadRequestException(BAD_REQUEST);
            }
        }
        menuRepository.save(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        // 更新父节点菜单数目
        updateSubCnt(resources.getPid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Menu resources) {
        if(resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Menu menu = menuRepository.findById(resources.getId()).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(),"Permission","id",resources.getId());

        if(YesNoEnum.YES.getCode().equals(resources.getFrame())){
            if (!(resources.getPath().toLowerCase().startsWith(HTTP_PRE)||resources.getPath().toLowerCase().startsWith(HTTPS_PRE))) {
                throw new BadRequestException(BAD_REQUEST);
            }
        }
        Menu menu1 = menuRepository.findByTitle(resources.getTitle());

        if(menu1 != null && !menu1.getId().equals(menu.getId())){
            throw new EntityExistException("菜单名称已存在");
        }

        if(resources.getPid()==null ||  resources.getPid().equals(0)){
            resources.setPid(null);
        }

        // 记录的父节点ID
        Integer oldPid = menu.getPid();
        Integer newPid = resources.getPid();

        if(StringUtils.isNotBlank(resources.getRouteName() )){
            menu1 = menuRepository.findByRouteName(resources.getRouteName());
            if(menu1 != null && !menu1.getId().equals(menu.getId())){
                throw new EntityExistException("路由名称已存在");
            }
        }
        menu.setTitle(resources.getTitle());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setFrame(resources.getFrame());
        menu.setPid(resources.getPid());
        menu.setMenuSort(resources.getMenuSort());
        menu.setCache(resources.getCache());
        menu.setCache(resources.getCache());
        menu.setVisible(resources.getVisible());
        menu.setStatus(resources.getStatus());
        menu.setRouteName(resources.getRouteName());
        menu.setQuery(resources.getQuery());
        menu.setPermission(resources.getPermission());
        menu.setType(resources.getType());
        menuRepository.save(menu);
        // 计算父级菜单节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // 清理缓存
        delCaches(resources.getId());
    }

    @Override
    public Set<Menu> getChildMenus(List<Menu> menuList, Set<Menu> menuSet) {
        for (Menu menu : menuList) {
            menuSet.add(menu);
            List<Menu> menus = menuRepository.findByPidOrderByMenuSort(menu.getId());
            if(CollUtil.isNotEmpty(menus)){
                getChildMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Menu> menuSet) {
        for (Menu menu : menuSet) {
            // 清理缓存
            delCaches(menu.getId());
            roleService.untiedMenu(menu.getId());
            menuRepository.logicDeleteById(menu.getId());
            updateSubCnt(menu.getPid());
        }
    }

    @Override
    public List<MenuDto> getMenus(Integer pid) {
        List<Menu> menus;
        if(pid != null && !pid.equals(0)){
            menus = menuRepository.findByPidOrderByMenuSort(pid);
        } else {
            menus = menuRepository.findByPidIsNullOrderByMenuSort();
        }
        return menuMapper.toDto(menus);
    }

    @Override
    public List<MenuDto> getSuperior(MenuDto menuDto, List<Menu> menus) {
        if(menuDto.getPid() == null){
            menus.addAll(menuRepository.findByPidIsNullOrderByMenuSort());
            return menuMapper.toDto(menus);
        }
        menus.addAll(menuRepository.findByPidOrderByMenuSort(menuDto.getPid()));
        return getSuperior(findById(menuDto.getPid()), menus);
    }

    @Override
    public List<MenuDto> buildTree(List<MenuDto> menuDtos) {
        List<MenuDto> trees = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (MenuDto menuDTO : menuDtos) {
            if (menuDTO.getPid() == null) {
                trees.add(menuDTO);
            }
            for (MenuDto it : menuDtos) {
                if (menuDTO.getId().equals(it.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if(trees.isEmpty()){
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).toList();
        }
        return trees;
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuDto> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO!=null){
                        List<MenuDto> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getRouteName())  ? menuDTO.getRouteName() : menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() :menuDTO.getPath());
                        menuVo.setHidden(YesNoEnum.NO.getCode().equals(menuDTO.getVisible()));
                        // 如果不是外链
                        if(!YesNoEnum.YES.getCode().equals(menuDTO.getFrame())){
                            if(menuDTO.getPid() == null){
                                menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent())?"Layout":menuDTO.getComponent());
                                // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                            }else if(menuDTO.getType() == 0){
                                menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent())?"ParentView":menuDTO.getComponent());
                            }else if(StringUtils.isNoneBlank(menuDTO.getComponent())){
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(),menuDTO.getIcon(),YesNoEnum.NO.getCode().equals(menuDTO.getCache()),""));
                        if(CollectionUtil.isNotEmpty(menuDtoList)){
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if(menuDTO.getPid() == null){
                            MenuVo menuVo1 = getMenuVo(menuDTO, menuVo);
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    @Override
    public Menu findOne(Integer id) {
        Menu menu = menuRepository.findById(id).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(),"Menu","id",id);
        return menu;
    }

    @Override
    public void download(List<MenuDto> menuDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MenuDto menuDTO : menuDtos) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("菜单标题", menuDTO.getTitle());
            map.put("菜单类型", menuDTO.getType() == null ? "目录" : menuDTO.getType() == 1 ? "菜单" : "按钮");
            map.put("权限标识", menuDTO.getPermission());
            map.put("外链菜单", YesNoEnum.YES.getCode().equals(menuDTO.getFrame()) ? YesNoEnum.YES.getDesc() :  YesNoEnum.NO.getDesc());
            map.put("菜单可见", YesNoEnum.YES.getCode().equals(menuDTO.getVisible()) ? YesNoEnum.YES.getDesc() :  YesNoEnum.NO.getDesc());
            map.put("是否缓存", YesNoEnum.YES.getCode().equals(menuDTO.getCache()) ? YesNoEnum.YES.getDesc() :  YesNoEnum.NO.getDesc());
            map.put("创建日期", menuDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    private void updateSubCnt(Integer menuId){
        if(menuId != null){
            int count = menuRepository.countByPid(menuId);
            menuRepository.updateSubCntById(count, menuId);
        }
    }

    /**
     * 清理缓存
     * @param id 菜单ID
     */
    public void delCaches(Integer id){
        List<User> users = userRepository.findByMenuId(id);
        redisUtils.del(CacheKey.MENU_ID + id);
        redisUtils.delByKeys(CacheKey.MENU_USER, users.stream().map(User::getId).collect(Collectors.toSet()));
        // 清除 Role 缓存
        List<Role> roles = roleService.findInMenuId(List.of(id));
        redisUtils.delByKeys(CacheKey.ROLE_ID, roles.stream().map(Role::getId).collect(Collectors.toSet()));
    }

    /**
     * 构建前端路由
     * @param menuDTO /
     * @param menuVo /
     * @return /
     */
    private static MenuVo getMenuVo(MenuDto menuDTO, MenuVo menuVo) {
        MenuVo menuVo1 = new MenuVo();
        menuVo1.setMeta(menuVo.getMeta());
        // 非外链
        if(!YesNoEnum.YES.getCode().equals(menuDTO.getFrame())){
            menuVo1.setPath("index");
            menuVo1.setName(menuVo.getName());
            menuVo1.setComponent(menuVo.getComponent());
        } else {
            menuVo1.setPath(menuDTO.getPath());
        }
        return menuVo1;
    }

    private MenuTreeDto toMenuTreeDto(Menu menu,Set<Integer> packageMenuIds) {
        MenuTreeDto menuTreeDto = new MenuTreeDto();
        menuTreeDto.setId(menu.getId());
        menuTreeDto.setTitle(menu.getTitle());
        menuTreeDto.setChildren(buildMenuChildren(menu.getId(), packageMenuIds));
        return menuTreeDto;
    }

    private List<MenuTreeDto> buildMenuChildren(Integer pid,Set<Integer> packageMenuIds) {
        List<Menu> child = menuRepository.findByPidAndStatusOrderByMenuSort(pid, CommonStatusEnum.ENABLE.getCode());
        if (child==null) {
            return null;
        }

        return child.stream().filter(s->packageMenuIds ==null || packageMenuIds.contains(s.getId())).map(s->toMenuTreeDto(s,packageMenuIds)).toList();
    }
}
