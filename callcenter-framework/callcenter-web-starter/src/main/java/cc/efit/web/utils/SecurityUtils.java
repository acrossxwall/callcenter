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
package cc.efit.web.utils;

import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.json.utils.JsonUtils;
import cc.efit.web.base.JwtUserVo;
import cc.efit.web.base.UserDataScope;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

import static cc.efit.web.enums.UserTypeEnum.ORG_ADMINISTRATOR;
import static cc.efit.web.enums.UserTypeEnum.SYSTEM_ADMINISTRATOR;

/**
 * 获取当前登录的用户
 *
 * @date 2019-01-17
 */
@Component
public class SecurityUtils {

    public static String header;

    public static String tokenStartWith;

    @Value("${jwt.header:Authorization}")
    public void setHeader(String header) {
        SecurityUtils.header = header;
    }

    @Value("${jwt.token-start-with:Bearer}")
    public void setTokenStartWith(String tokenStartWith) {
        SecurityUtils.tokenStartWith = tokenStartWith;
    }

    /**
     * 获取当前登录的用户
     * @return UserDetails
     */
    public static UserDetails getCurrentUser() {
        UserDetailsService userDetailsService = SpringBeanHolder.getBean(UserDetailsService.class);
        return userDetailsService.loadUserByUsername(getCurrentUsername());
    }

    /**
     * 获取当前用户的数据权限
     * @return /
     */
    public static UserDataScope getCurrentUserDataScope(){
        UserDetails userDetails = getCurrentUser();
        // 将 Java 对象转换为 JSONObject 对象
        String json =  JsonUtils.toJsonString(userDetails);
        JwtUserVo userVo = JsonUtils.parseObject(json, JwtUserVo.class);
        assert userVo != null;
        return userVo.dataScopes();
    }

    /**
     * 获取用户ID
     * @return 系统用户ID
     */
    public static Integer getCurrentUserId() {
        return getCurrentUserId(getToken());
    }

    public static Integer getCurrentDeptId() {
        return getCurrentDeptId(getToken());
    }

    /**
     * 获取用户ID
     * @return 系统用户ID
     */
    public static Integer getCurrentUserId(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return Integer.valueOf(jwt.getPayload("userId").toString());
    }

    public static Integer getCurrentDeptId(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        Object obj = jwt.getPayload("deptId");
        return obj==null? null : Integer.valueOf(obj.toString());
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername() {
        return getCurrentUsername(getToken());
    }

    public static Integer getCurrentUserOrgId() {
        try {
            JWT jwt = JWTUtil.parseToken(getToken());
            Object obj = jwt.getPayload("orgId");
            return obj==null?null: Integer.valueOf( obj.toString());
        }catch (Exception e){
            return null;
        }
    }

    public static boolean getCurrentUserIsSystemAdmin() {
        Integer userType = getCurrentUserType();
        return SYSTEM_ADMINISTRATOR.getCode().equals(userType);
    }

    public static boolean getCurrentUserIsOrgAdmin(){
        Integer userType = getCurrentUserType();
        return ORG_ADMINISTRATOR.getCode().equals(userType);
    }


    public static Integer getCurrentUserType() {
        try {
            JWT jwt = JWTUtil.parseToken(getToken());
            Object obj = jwt.getPayload("userType");
            return obj == null ? null : Integer.valueOf(obj.toString())  ;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayload("sub").toString();
    }

    /**
     * 获取Token
     * @return /
     */
    public static String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith(tokenStartWith)) {
            // 去掉令牌前缀
            return bearerToken.replace(tokenStartWith, "");
        }
        return null;
    }
}
