package cc.efit.system.rest;

import cc.efit.web.enums.UserTypeEnum;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.system.domain.User;
import cc.efit.modules.system.service.UserService;
import cc.efit.modules.system.service.dto.UserQueryCriteria;
import cc.efit.res.R;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.system.domain.SysOrg;
import cc.efit.system.service.SysOrgService;
import cc.efit.system.service.dto.SysOrgQueryCriteria;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cc.efit.annotation.Log;
import org.springframework.data.domain.Pageable;
import java.io.IOException;

/**
 * 多租户Controller
 * 
 * @author across
 * @date 2025-08-06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/system/org")
public class SysOrgController {

    private final SysOrgService sysOrgService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    /**
     * 查询多租户列表
     */
    @PreAuthorize("@cc.check('system:org:list')")
    @GetMapping("/list")
    public R list(SysOrgQueryCriteria criteria, Pageable pageable) {
        return R.ok(sysOrgService.queryAll(criteria,pageable));
    }

    /**
     * 导出多租户列表
     */
    @PreAuthorize("@cc.check('system:org:list')")
    @Log("多租户导出" )
    @PostMapping("/export")
    public void exportSysOrg(HttpServletResponse response, SysOrgQueryCriteria criteria) throws IOException {
        sysOrgService.download(sysOrgService.queryAll(criteria), response);
    }

    /**
     * 获取多租户详细信息
     */
    @PreAuthorize("@cc.check('system:org:list')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(sysOrgService.selectSysOrgById(id));
    }

    /**
     * 新增多租户
     */
    @PreAuthorize("@cc.check('system:org:add')")
    @Log("多租户新增" )
    @PostMapping
    public R add(@RequestBody SysOrg sysOrg) {
        sysOrgService.insertSysOrg(sysOrg);
        return R.ok();
    }

    /**
     * 修改多租户
     */
    @PreAuthorize("@cc.check('system:org:edit')")
    @Log("多租户修改" )
    @PutMapping
    public R edit(@RequestBody SysOrg sysOrg) {
        sysOrgService.updateSysOrg(sysOrg);
        return R.ok();
    }

    /**
     * 删除多租户
     */
    @PreAuthorize("@cc.check('system:org:remove')")
    @Log("多租户删除" )
	@DeleteMapping("/{ids}")
    public R removeIds(@PathVariable Integer[] ids) {
        sysOrgService.deleteSysOrgByIds(ids);
        return R.ok();
    }

    /**
     * 删除多租户
     */
    @PreAuthorize("@cc.check('system:org:remove')")
    @Log("多租户删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        sysOrgService.deleteSysOrgById(id);
        return R.ok();
    }
    @PreAuthorize("@cc.check('system:org:list')")
    @GetMapping("/all")
    public R getEnableOrgList(){
        return R.ok(sysOrgService.getEnableOrgList());
    }

    @Operation(summary="查询租户管理员用户")
    @GetMapping("/users")
    @PreAuthorize("@cc.check('system:org:list')")
    public R queryUser(UserQueryCriteria criteria, Pageable pageable){
        criteria.setUserType(UserTypeEnum.ORG_ADMINISTRATOR.getCode());
        return R.ok(userService.queryAll(criteria,pageable));
    }

    @Log("新增租户管理员")
    @Operation(summary="新增租户管理员")
    @PostMapping("/users")
    @PreAuthorize("@cc.check('system:org:add')")
    public R createUser(@Validated @RequestBody User resources){
        if (resources.getOrgId()==null || resources.getPassword()==null) {
            throw new BadRequestException("参数不正确");
        }
        resources.setPassword(passwordEncoder.encode(resources.getPassword()));
        resources.setUserType(UserTypeEnum.ORG_ADMINISTRATOR.getCode());
        userService.create(resources);
        return R.ok();
    }

    @Log("修改租户管理员")
    @Operation(summary="修改租户管理员")
    @PutMapping("/users")
    @PreAuthorize("@cc.check('system:org:edit')")
    public R updateUser(@Validated(User.Update.class) @RequestBody User resources) throws Exception {
        userService.update(resources);
        return R.ok();
    }

    @Log("修改租户")
    @Operation(summary="修改租户状态")
    @PutMapping("/changeStatus")
    @PreAuthorize("@cc.check('system:org:edit')")
    public R changeOrgStatus(@RequestBody SysOrg resources){
        sysOrgService.updateOrgStatus(resources);
        return R.ok();
    }
}
