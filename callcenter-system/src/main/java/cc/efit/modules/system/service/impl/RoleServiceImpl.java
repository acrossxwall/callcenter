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
import cc.efit.web.enums.UserTypeEnum;
import cc.efit.modules.security.service.dto.JwtUserDto;
import cc.efit.modules.system.service.dto.*;
import cc.efit.modules.system.service.mapstruct.UserInfoMapper;
import cc.efit.redis.utils.RedisUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.security.service.UserCacheManager;
import cc.efit.modules.security.service.dto.AuthorityDto;
import cc.efit.modules.system.domain.Menu;
import cc.efit.modules.system.domain.Role;
import cc.efit.exception.EntityExistException;
import cc.efit.modules.system.domain.User;
import cc.efit.modules.system.repository.RoleRepository;
import cc.efit.modules.system.repository.UserRepository;
import cc.efit.modules.system.service.RoleService;
import cc.efit.modules.system.service.mapstruct.RoleMapper;
import cc.efit.modules.system.service.mapstruct.RoleSmallMapper;
import cc.efit.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 * @date 2018-12-03
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleSmallMapper roleSmallMapper;
    private final RedisUtils redisUtils;
    private final UserRepository userRepository;
    private final UserCacheManager userCacheManager;
    private final UserInfoMapper userInfoMapper;
    @Override
    public List<RoleDto> queryAll() {
        return roleMapper.toDto(roleRepository.findByStatus(CommonStatusEnum.ENABLE.getCode()));
    }

    @Override
    public List<RoleDto> queryAll(RoleQueryCriteria criteria) {
        return roleMapper.toDto(roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public PageResult<RoleDto> queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        Page<Role> page = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    public RoleDto findById(Integer id) {
        String key = CacheKey.ROLE_ID + id;
        Role role = redisUtils.get(key, Role.class);
        if (role == null) {
            role = roleRepository.findById(id).orElseGet(Role::new);
            ValidationUtil.isNull(role.getId(), "Role", "id", id);
            redisUtils.set(key, role, 1, TimeUnit.DAYS);
        }
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Role resources) {
        if (roleRepository.findByName(resources.getName()) != null) {
            throw new EntityExistException("角色名称已存在");
        }
        roleRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        Role role = roleRepository.findById(resources.getId()).orElseGet(Role::new);
        ValidationUtil.isNull(role.getId(), "Role", "id", resources.getId());

        Role role1 = roleRepository.findByName(resources.getName());

        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException("角色名称已存在");
        }
        role.setName(resources.getName());
        role.setDescription(resources.getDescription());
        role.setDataScope(resources.getDataScope());
        role.setDepts(resources.getDepts());
        role.setStatus(resources.getStatus());
        role.setRoleKey(resources.getRoleKey());
        role.setRoleSort(resources.getRoleSort());
        role.setMenus(resources.getMenus());
        roleRepository.save(role);
        // 更新相关缓存
        delCaches(role.getId(), null);
    }

    @Override
    public void updateMenu(Role resources, RoleDto roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        List<User> users = userRepository.findByRoleId(role.getId());
        // 更新菜单
        role.setMenus(resources.getMenus());
        delCaches(resources.getId(), users);
        roleRepository.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Integer menuId) {
        // 更新菜单
        roleRepository.untiedMenu(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Integer> ids) {
        for (Integer id : ids) {
            // 更新相关缓存
            delCaches(id, null);
            roleRepository.logicDeleteById(id);
        }
    }

    @Override
    public List<RoleSmallDto> findByUsersId(Integer userId) {
        String key = CacheKey.ROLE_USER + userId;
        List<RoleSmallDto> roles = redisUtils.getList(key, RoleSmallDto.class);
        if (CollUtil.isEmpty(roles)) {
            roles = roleSmallMapper.toDto(new ArrayList<>(roleRepository.findByUserId(userId)));
            redisUtils.set(key, roles, 1, TimeUnit.DAYS);
        }
        return roles;
    }

    @Override
    public Integer findByRoles(Set<Role> roles) {
        return 0;
    }

    @Override
    public void buildPermissions(UserDto user, JwtUserDto jwtUserDto) {
        String key = CacheKey.ROLE_AUTH + user.getId();
        List<AuthorityDto> authorityDtos = redisUtils.getList(key, AuthorityDto.class);
        if (CollUtil.isEmpty(authorityDtos)) {
            // 如果是管理员直接返回
            if (UserTypeEnum.SYSTEM_ADMINISTRATOR.getCode().equals(user.getUserType())) {
                authorityDtos = new ArrayList<>();
                AuthorityDto authorityDto = new AuthorityDto("admin");
                authorityDtos.add(authorityDto);
                jwtUserDto.setAuthorities(authorityDtos);
                jwtUserDto.setRoles(Set.of("admin"));
                jwtUserDto.setPermission(Set.of("*:*:*"));
                return  ;
            }
            Set<Role> roles = roleRepository.findByUserId(user.getId());
            Set<String> permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                    .map(Menu::getPermission)
                    .filter(StringUtils::isNotBlank).collect(Collectors.toSet());
            authorityDtos = permissions.stream().map(AuthorityDto::new)
                    .toList();
            jwtUserDto.setPermission(permissions);
            jwtUserDto.setAuthorities(authorityDtos);
            jwtUserDto.setRoles(roles.stream()
                    .map(role->Arrays.asList(role.getRoleKey().trim().split(",") ))
                    .map(String::valueOf).collect(Collectors.toSet()));
            redisUtils.set(key, authorityDtos, 1, TimeUnit.HOURS);
        }
    }

    @Override
    public void download(List<RoleDto> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDto role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void verification(Set<Integer> ids) {
        if (userRepository.countByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public List<Role> findInMenuId(List<Integer> menuIds) {
        return roleRepository.findInMenuId(menuIds);
    }

    @Override
    public void updateRoleStatus(Role resources) {
        roleRepository.updateRoleStatus(resources.getId(), resources.getStatus());
    }

    @Override
    public PageResult<UserInfoDto> queryAllUserByRoleId(UserQueryCriteria criteria, Pageable pageable) {
        Page<User> page = userRepository.findAll( (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(userInfoMapper::toDto));
    }

    @Override
    @Transactional
    public void insertAuthUsers(Integer roleId, Integer[] userIds,Integer orgId) {

        for (Integer id :userIds) {
            roleRepository.insertUserRole(id,roleId, orgId);
        }
    }

    @Override
    public void clearOrgPackageCache(Set<Integer> add, Set<Integer> delete,Set<Integer> orgIds) {
        orgIds.forEach(orgId -> clearOrgPackageCache(add,delete, orgId));
    }

    @Override
    public void clearOrgPackageCache(Set<Integer> add, Set<Integer> delete, Integer  orgId) {
        if (CollectionUtil.isNotEmpty(add)) {
            //构建sys_role_menu 仅增加机构管理员的对应的角色id即可,其他角色让它自己分配
            Integer roleId = roleRepository.findOrgAdminRoleByOrgId(orgId);
            if (roleId!=null) {
               add.forEach(id -> roleRepository.insertRoleMenu(id, roleId,orgId));
            }
        }
        if (CollectionUtil.isNotEmpty(delete)) {
            //删除的menu id 不为空，查出机构org 当前menu id对应的所有role 并清空缓存
            List<Integer> list = roleRepository.findRoleIdByMenuIdsAndOrgId(delete, orgId);
            if (CollectionUtil.isNotEmpty(list)) {
                list.stream().distinct().forEach(s-> delCaches(s,null));
            }
            delete.forEach(id-> roleRepository.untiedMenu(id, orgId));
        }
    }

    /**
     * 清理缓存
     * @param id /
     */
    public void delCaches(Integer id, List<User> users) {
        users = CollectionUtil.isEmpty(users) ? userRepository.findByRoleId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheManager.cleanUserCache(item.getUsername()));
            Set<Integer> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            redisUtils.delByKeys(CacheKey.DATA_USER, userIds);
            redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtils.delByKeys(CacheKey.ROLE_AUTH, userIds);
            redisUtils.delByKeys(CacheKey.ROLE_USER, userIds);
        }
        redisUtils.del(CacheKey.ROLE_ID + id);
    }
}
