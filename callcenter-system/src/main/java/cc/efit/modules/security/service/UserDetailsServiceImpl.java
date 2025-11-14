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
package cc.efit.modules.security.service;

import cc.efit.enums.CommonStatusEnum;
import cc.efit.org.GlobalPermissionHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.security.service.dto.AuthorityDto;
import cc.efit.modules.security.service.dto.JwtUserDto;
import cc.efit.modules.system.service.DataService;
import cc.efit.modules.system.service.RoleService;
import cc.efit.modules.system.service.UserService;
import cc.efit.modules.system.service.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 
 * @date 2018-11-22
 */
@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;
    private final DataService dataService;
    private final UserCacheManager userCacheManager;

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        boolean ignore = GlobalPermissionHolder.isIgnore();
        try {
            //全局忽略，否则会产生stack overflow error
            GlobalPermissionHolder.setIgnore(true);
            JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
            if(jwtUserDto == null){
                UserDto user = userService.getLoginData(username);
                if (user == null) {
                    throw new BadRequestException("用户不存在");
                } else {
                    if (!CommonStatusEnum.ENABLE.getCode().equals(user.getStatus())) {
                        throw new BadRequestException("账号未激活！");
                    }
                    jwtUserDto = new JwtUserDto(user, dataService.getDeptIds(user) );
                    // 获取用户的权限
                    roleService.buildPermissions(user,jwtUserDto);
                    // 添加缓存数据
                    userCacheManager.addUserCache(username, jwtUserDto);
                }
            }
            return jwtUserDto;
        }finally {
            GlobalPermissionHolder.setIgnore(ignore);
        }

    }
}
