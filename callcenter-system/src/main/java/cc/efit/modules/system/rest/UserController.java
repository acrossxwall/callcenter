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

import cc.efit.web.enums.UserTypeEnum;
import cc.efit.res.R;
import cc.efit.web.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.annotation.Log;
import cc.efit.config.properties.RsaProperties;
import cc.efit.modules.system.domain.Dept;
import cc.efit.modules.system.domain.User;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.system.domain.vo.UserPassVo;
import cc.efit.modules.system.service.DeptService;
import cc.efit.modules.system.service.dto.UserDto;
import cc.efit.modules.system.service.dto.UserQueryCriteria;
import cc.efit.modules.system.service.VerifyService;
import cc.efit.utils.*;
import cc.efit.modules.system.service.UserService;
import cc.efit.enums.CodeEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 
 * @date 2018-11-23
 */
@Tag(name = "系统：用户管理")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final DeptService deptService;
    private final VerifyService verificationCodeService;

    @Operation(summary="导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('system:user:list')")
    public void exportUser(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    @Operation(summary="查询用户")
    @GetMapping
    @PreAuthorize("@cc.check('system:user:list', 'system:roles:edit')")
    public R queryUser(UserQueryCriteria criteria, Pageable pageable){
        criteria.setUserType(UserTypeEnum.NORMAL.getCode());
        if (!ObjectUtils.isEmpty(criteria.getUserDeptId())) {
            criteria.getDeptIds().add(criteria.getUserDeptId());
            // 先查找是否存在子节点
            List<Dept> data = deptService.findByPid(criteria.getUserDeptId());
            // 然后把子节点的ID都加入到集合中
            criteria.getDeptIds().addAll(deptService.getDeptChildren(data));
        }
        return R.ok(userService.queryAll(criteria,pageable));
    }

    @Log("新增用户")
    @Operation(summary="新增用户")
    @PostMapping
    @PreAuthorize("@cc.check('system:user:add')")
    public R createUser(@Validated @RequestBody User resources){
        //checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode(resources.getPassword()));
        resources.setUserType(UserTypeEnum.NORMAL.getCode());
        userService.create(resources);
        return R.ok( );
    }

    @Log("修改用户")
    @Operation(summary="修改用户")
    @PutMapping
    @PreAuthorize("@cc.check('system:user:edit')")
    public R updateUser(@Validated(User.Update.class) @RequestBody User resources) throws Exception {
        // checkLevel(resources);
        userService.update(resources);
        return R.ok();
    }

    @Log("修改用户：个人中心")
    @Operation(summary="修改用户：个人中心")
    @PutMapping(value = "center")
    public R centerUser(@Validated(User.Update.class) @RequestBody User resources){
        if(!resources.getId().equals(SecurityUtils.getCurrentUserId())){
            throw new BadRequestException("不能修改他人资料");
        }
        userService.updateCenter(resources);
        return R.ok();
    }

    @Log("删除用户")
    @Operation(summary="删除用户")
    @DeleteMapping
    @PreAuthorize("@cc.check('system:user:remove')")
    public R deleteUser(@RequestBody Set<Integer> ids){
        userService.delete(ids);
        return R.ok();
    }

    @Log("删除单个用户")
    @Operation(summary="删除单个用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("@cc.check('system:user:remove')")
    public R deleteUserById(@PathVariable("id") Integer id){
        userService.delete(Set.of(id));
        return R.ok();
    }

    @Operation(summary="修改密码")
    @PostMapping(value = "/updatePass")
    public R updateUserPass(@RequestBody UserPassVo passVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,passVo.getNewPass());
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        if(!passwordEncoder.matches(oldPass, user.getPassword())){
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if(passwordEncoder.matches(newPass, user.getPassword())){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(user.getUsername(),passwordEncoder.encode(newPass));
        return R.ok();
    }

    @Operation(summary="重置密码")
    @PutMapping(value = "/resetPwd")
    public R resetPwd(@RequestBody User resources) throws Exception {
        String pwd = passwordEncoder.encode(resources.getPassword());
        userService.resetPwd(Set.of(resources.getId()), pwd);
        return R.ok();
    }

    @Operation(summary="修改头像")
    @PostMapping(value = "/updateAvatar")
    public R updateUserAvatar(@RequestParam MultipartFile avatar){
        return R.ok(userService.updateAvatar(avatar));
    }

    @Log("修改邮箱")
    @Operation(summary="修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public R updateUserEmail(@PathVariable String code,@RequestBody User user) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,user.getPassword());
        UserDto userDto = userService.findByName(SecurityUtils.getCurrentUsername());
        if(!passwordEncoder.matches(password, userDto.getPassword())){
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userService.updateEmail(userDto.getUsername(),user.getEmail());
        return R.ok();
    }
    @Log("修改用户")
    @Operation(summary="修改用户")
    @PutMapping("/changeStatus")
    @PreAuthorize("@cc.check('system:user:edit')")
    public R changeUserStatus(@RequestBody User resources){
        userService.updateUserStatus(resources);
        return R.ok();
    }
    @Operation(summary="查询用户信息")
    @GetMapping("/get/{id}")
    @PreAuthorize("@cc.check('system:user:edit')")
    public R getUserInfoById(@PathVariable("id") Integer id) {
        return R.ok(userService.findUserInfoById(id));
    }

    @GetMapping("/profile")
    public R getUserProfile() {
        Integer id = SecurityUtils.getCurrentUserId();
        return R.ok(userService.findUserInfoById(id));
    }
}
