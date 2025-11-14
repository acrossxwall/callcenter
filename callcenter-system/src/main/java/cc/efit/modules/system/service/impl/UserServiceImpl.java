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
import cc.efit.modules.system.repository.RoleRepository;
import cc.efit.modules.system.service.mapstruct.UserInfoMapper;
import cc.efit.org.GlobalPermissionHolder;
import cc.efit.redis.utils.RedisUtils;
import cc.efit.web.enums.UserTypeEnum;
import cc.efit.web.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import cc.efit.utils.PageResult;
import cc.efit.config.properties.FileProperties;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.security.service.OnlineUserService;
import cc.efit.modules.security.service.UserCacheManager;
import cc.efit.modules.system.domain.User;
import cc.efit.exception.EntityExistException;
import cc.efit.exception.EntityNotFoundException;
import cc.efit.modules.system.repository.UserRepository;
import cc.efit.modules.system.service.UserService;
import cc.efit.modules.system.service.dto.*;
import cc.efit.modules.system.service.mapstruct.UserMapper;
import cc.efit.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @date 2018-11-23
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;
    private final FileProperties properties;
    private final RedisUtils redisUtils;
    private final UserCacheManager userCacheManager;
    private final OnlineUserService onlineUserService;
    private final RoleRepository roleRepository;
    
    @Override
    public PageResult<UserInfoDto> queryAll(UserQueryCriteria criteria, Pageable pageable) {
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(userInfoMapper::toDto));
    }

    @Override
    public List<UserDto> queryAll(UserQueryCriteria criteria) {
        List<User> users = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return userMapper.toDto(users);
    }

    @Override
    public UserDto findById(Integer id) {
        String key = CacheKey.USER_ID + id;
        User user = redisUtils.get(key, User.class);
        if (user == null) {
            user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("用户不存在"));
            redisUtils.set(key, user, 1, TimeUnit.DAYS);
        }
        return userMapper.toDto(user);
    }

    @Override
    public UserInfoDto findUserInfoById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("用户不存在"));
        return userInfoMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(User resources) {
        validUserNameExists(resources,null);
        userRepository.save(resources);
        if(UserTypeEnum.ORG_ADMINISTRATOR.getCode().equals(resources.getUserType())){
            //创建组织管理员角色
            Integer roleId = roleRepository.findOrgAdminRoleByOrgId(resources.getOrgId());
            if (roleId !=null) {
                roleRepository.insertUserRole(resources.getId(), roleId,resources.getOrgId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) throws Exception {
        User user = userRepository.findById(resources.getId()).orElseThrow(() -> new EntityNotFoundException("用户不存在"));
        validUserNameExists(resources, user);

        // 如果用户的角色改变
        if (!resources.getRoles().equals(user.getRoles())) {
            redisUtils.del(CacheKey.DATA_USER + resources.getId());
            redisUtils.del(CacheKey.MENU_USER + resources.getId());
            redisUtils.del(CacheKey.ROLE_AUTH + resources.getId());
            redisUtils.del(CacheKey.ROLE_USER + resources.getId());
        }
        // 修改部门会影响 数据权限
        if (!Objects.equals(resources.getDept(),user.getDept())) {
            redisUtils.del(CacheKey.DATA_USER + resources.getId());
        }
        // 如果用户被禁用，则清除用户登录信息
        if(!CommonStatusEnum.ENABLE.getCode().equals(resources.getStatus())){
            onlineUserService.kickOutForUsername(resources.getUsername());
        }
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setStatus(resources.getStatus());
        user.setRoles(resources.getRoles());
        user.setDept(resources.getDept());
        user.setJobs(resources.getJobs());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setGender(resources.getGender());
        userRepository.save(user);
        // 清除缓存
        delCaches(user.getId(), user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(User resources) {
        boolean globalIgnore = GlobalPermissionHolder.isIgnore();
        try {
            //需要忽略权限，
            GlobalPermissionHolder.setIgnore(true);
            userRepository.updateCenterInfo(
                    resources.getId(),
                    resources.getNickName()==null?"": resources.getNickName(),
                    resources.getPhone()==null?"": resources.getPhone(),
                    resources.getGender()==null?"":resources.getGender(),
                    resources.getEmail()==null?"":resources.getEmail()
            );
            delCaches(resources.getId(), SecurityUtils.getCurrentUsername()) ;
        }finally {
            GlobalPermissionHolder.setIgnore(globalIgnore);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Integer> ids) {
        for (Integer id : ids) {
            // 清理缓存
            UserDto user = findById(id);
            delCaches(user.getId(), user.getUsername());
            userRepository.logicDeleteById(id);
        }
    }

    @Override
    public UserDto findByName(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new EntityNotFoundException("用户名不存在");
        } else {
            return userMapper.toDto(user);
        }
    }

    @Override
    public UserDto getLoginData(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            return null;
        } else {
            return userMapper.toDto(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userRepository.updatePass(username, pass, new Date());
        flushCache(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(Set<Integer> ids, String pwd) {
        List<User> users = userRepository.findAllById(ids);
        // 清除缓存
        users.forEach(user -> {
            // 清除缓存
            flushCache(user.getUsername());
            // 强制退出
            onlineUserService.kickOutForUsername(user.getUsername());
        });
        // 重置密码
        userRepository.resetPwd(ids, pwd);
    }

    @Override
    @Transactional
    public void updateUserLoginInfo(String username,String ip) {
        userRepository.updateUserLoginInfo(username,ip,new Date());
    }

    @Override
    public void updateUserStatus(User resources) {
        userRepository.updateUserStatus(resources.getId(),resources.getStatus());
    }

    @Override
    public void deleteUserByOrgId(Integer orgId) {
        List<User> orgUser = userRepository.findByOrgId(orgId);
        if (orgUser != null && !orgUser.isEmpty()) {
            orgUser.forEach(user -> {
                delCaches(user.getId(), user.getUsername());
                userRepository.logicDeleteById(user.getId());
            });
        }
    }

    @Override
    public void updateUserStatusByOrgId(Integer orgId, Integer status) {
        userRepository.updateUserStatusByOrgId(orgId, status);
        if (CommonStatusEnum.DISABLE.getCode().equals(status)) {
            List<User> orgUser = userRepository.findByOrgId(orgId);
            if (orgUser != null && !orgUser.isEmpty()) {
                orgUser.forEach(user -> delCaches(user.getId(), user.getUsername()));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {
        // 文件大小验证
        FileUtil.checkSize(properties.getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        if(fileType != null && !image.contains(fileType)){
            throw new BadRequestException("文件格式错误！, 仅支持 " + image +" 格式");
        }
        boolean ignore = false;
        try {
            ignore = GlobalPermissionHolder.isIgnore();
            GlobalPermissionHolder.setIgnore(true);
            Integer id = SecurityUtils.getCurrentUserId();
            String oldPath = userRepository.selectOldUserAvatarInfo(id);
            File file = FileUtil.upload(multipartFile, properties.getPath().getAvatar());
            assert file != null;
            String path = "/avatar/" + file.getName();
            userRepository.updateUserAvatarInfo(id, path,  file.getPath());
            if (StringUtils.isNotBlank(oldPath)) {
                FileUtil.del(oldPath);
            }
            String username = SecurityUtils.getCurrentUsername();
            flushCache(username);
            return new HashMap<>(1) {{
                put("avatar", path);
            }};
        }finally {
            GlobalPermissionHolder.setIgnore(ignore);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userRepository.updateEmail(username, email);
        flushCache(username);
    }

    @Override
    public void download(List<UserDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDto userDTO : queryAll) {
            List<String> roles = userDTO.getRoles().stream().map(RoleSmallDto::getName).toList();
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("角色", roles);
            map.put("部门", userDTO.getDept().getName());
            map.put("岗位", userDTO.getJobs().stream().map(JobSmallDto::getName).toList());
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", CommonStatusEnum.ENABLE.getCode().equals(userDTO.getStatus())  ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("修改密码的时间", userDTO.getPwdUpdateTime());
            map.put("最后登录id",userDTO.getLoginIp());
            map.put("最后登录时间", userDTO.getLoginDate());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Integer id, String username) {
        redisUtils.del(CacheKey.USER_ID + id);
        flushCache(username);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheManager.cleanUserCache(username);
    }

    private void validUserNameExists(User resources,User user) {
        boolean ignore = GlobalPermissionHolder.isIgnore();
        try {
            GlobalPermissionHolder.setIgnore(true);
            User user1 = userRepository.findByUsername(resources.getUsername());
            if (user1==null) {
                return;
            }
            if (user==null ||  !user.getId().equals(user1.getId())) {
                throw new EntityExistException("用户名已存在");
            }
        }finally {
            GlobalPermissionHolder.setIgnore(ignore);
        }
    }
}
