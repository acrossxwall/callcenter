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
package cc.efit.modules.security.service.dto;

import cc.efit.enums.CommonStatusEnum;
import cc.efit.web.base.UserDataScope;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import cc.efit.modules.system.service.dto.UserDto;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Set;

/**
 *
 * @date 2018-11-23
 */
@Getter
@Setter
public class JwtUserDto implements UserDetails {
    public JwtUserDto(){}

    @Schema(name = "用户")
    private  UserDto user;

    @Schema(name = "数据权限")
    private UserDataScope dataScopes;

    @Schema(name = "角色权限")
    private  List<AuthorityDto> authorities;

    private Set<String> roles;

    private Set<String> permission;

    public JwtUserDto(UserDto user, UserDataScope dataScopes) {
        this.user = user;
        this.dataScopes = dataScopes;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return CommonStatusEnum.ENABLE.getCode().equals(user.getStatus());
    }

}
