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
package cc.efit.modules.security.rest;

import cc.efit.data.permission.DataPermissionIgnore;
import cc.efit.modules.security.service.UserCacheManager;
import cc.efit.modules.system.service.UserService;
import cc.efit.org.permission.OrgPermissionIgnore;
import cc.efit.res.R;
import cc.efit.utils.StringUtilsExternal;
import cc.efit.web.utils.SecurityUtils;
import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cc.efit.annotation.Log;
import cc.efit.annotation.rest.AnonymousDeleteMapping;
import cc.efit.annotation.rest.AnonymousGetMapping;
import cc.efit.annotation.rest.AnonymousPostMapping;
import cc.efit.config.properties.RsaProperties;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.security.config.CaptchaConfig;
import cc.efit.modules.security.config.enums.LoginCodeEnum;
import cc.efit.modules.security.config.LoginProperties;
import cc.efit.modules.security.config.SecurityProperties;
import cc.efit.modules.security.security.TokenProvider;
import cc.efit.modules.security.service.UserDetailsServiceImpl;
import cc.efit.modules.security.service.dto.AuthUserDto;
import cc.efit.modules.security.service.dto.JwtUserDto;
import cc.efit.modules.security.service.OnlineUserService;
import cc.efit.utils.RsaUtils;
import cc.efit.redis.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @date 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "系统：系统授权接口")
public class AuthController {
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final LoginProperties loginProperties;
    private final CaptchaConfig captchaConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final UserCacheManager userCacheManager;

    @Log("用户登录")
    @Operation(summary="登录授权")
    @AnonymousPostMapping(value = "/login")
    @OrgPermissionIgnore
    @DataPermissionIgnore
    public R login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 查询验证码
        String code = redisUtils.get(authUser.getUuid(), String.class);
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        // 获取用户信息
        JwtUserDto jwtUser = userDetailsService.loadUserByUsername(authUser.getUsername());
        // 验证用户密码
        if (!passwordEncoder.matches(password, jwtUser.getPassword())) {
            throw new BadRequestException("登录密码错误");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(jwtUser);
        // 返回 token 与 用户信息
        if (loginProperties.isSingleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(authUser.getUsername());
        }
        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);
        userService.updateUserLoginInfo(authUser.getUsername(), StringUtilsExternal.getIp(request));
        // 返回登录信息
        return R.success(token);
    }

    @Operation(summary="获取用户信息")
    @GetMapping(value = "/info")
    public R  getUserInfo() {
        JwtUserDto jwtUser = (JwtUserDto) SecurityUtils.getCurrentUser();
        Map<String,Object> ret = new HashMap<>();
        ret.put("user", jwtUser.getUser());
        ret.put("roles", jwtUser.getRoles());
        ret.put("permissions", jwtUser.getPermission());
        ret.put("isDefaultModifyPwd", initPasswordIsModify(jwtUser.getUser().getPwdUpdateTime()));
        ret.put("isPasswordExpired", passwordIsExpiration(jwtUser.getUser().getPwdUpdateTime()));
        return R.ok(ret);
    }

    @Operation(summary="获取验证码")
    @AnonymousGetMapping(value = "/code")
    public R getCode() {
        // 获取运算的结果
        Captcha captcha = captchaConfig.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, captchaConfig.getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return R.ok(imgResult);
    }

    @Operation(summary="退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public R logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        JwtUserDto jwtUser = (JwtUserDto) SecurityUtils.getCurrentUser();
        String username = jwtUser.getUsername();
        userCacheManager.cleanUserCache(username);
        return R.success();
    }


    public boolean initPasswordIsModify(Date pwdUpdateDate){
//        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
//        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
        //TODO 初始化密码是否修改
        return false;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Date pwdUpdateDate){
//        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
//        if (passwordValidateDays != null && passwordValidateDays > 0)
//        {
//            if (StringUtils.isNull(pwdUpdateDate))
//            {
//                // 如果从未修改过初始密码，直接提醒过期
//                return true;
//            }
//            Date nowDate = DateUtils.getNowDate();
//            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
//        }
        //TODO 密码是否过期
        return false;
    }
}
