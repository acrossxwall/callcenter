package cc.efit.system.rest;

import cc.efit.res.R;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.system.domain.SysOrgPackageMenus;
import cc.efit.system.service.SysOrgPackageMenusService;
import cc.efit.system.service.dto.SysOrgPackageMenusQueryCriteria;

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
 * 租户套餐菜单关联表Controller
 * 
 * @author across
 * @date 2025-10-28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/system/menus")
public class SysOrgPackageMenusController {

    private final SysOrgPackageMenusService sysOrgPackageMenusService;

    /**
     * 查询租户套餐菜单关联表列表
     */
    @PreAuthorize("@cc.check('system:menus:list')")
    @GetMapping("/list")
    public R list(SysOrgPackageMenusQueryCriteria criteria, Pageable pageable) {
        return R.ok(sysOrgPackageMenusService.queryAll(criteria,pageable));
    }

    /**
     * 导出租户套餐菜单关联表列表
     */
    @PreAuthorize("@cc.check('system:menus:export')")
    @Log("租户套餐菜单关联表导出" )
    @PostMapping("/export")
    public void exportSysOrgPackageMenus(HttpServletResponse response, SysOrgPackageMenusQueryCriteria criteria) throws IOException {
        sysOrgPackageMenusService.download(sysOrgPackageMenusService.queryAll(criteria), response);
    }

    /**
     * 获取租户套餐菜单关联表详细信息
     */
    @PreAuthorize("@cc.check('system:menus:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(sysOrgPackageMenusService.selectSysOrgPackageMenusById(id));
    }

    /**
     * 新增租户套餐菜单关联表
     */
    @PreAuthorize("@cc.check('system:menus:add')")
    @Log("租户套餐菜单关联表新增" )
    @PostMapping
    public R add(@RequestBody SysOrgPackageMenus sysOrgPackageMenus) {
        sysOrgPackageMenusService.insertSysOrgPackageMenus(sysOrgPackageMenus);
        return R.ok();
    }

    /**
     * 修改租户套餐菜单关联表
     */
    @PreAuthorize("@cc.check('system:menus:edit')")
    @Log("租户套餐菜单关联表修改" )
    @PutMapping
    public R edit(@RequestBody SysOrgPackageMenus sysOrgPackageMenus) {
        sysOrgPackageMenusService.updateSysOrgPackageMenus(sysOrgPackageMenus);
        return R.ok();
    }

    /**
     * 删除租户套餐菜单关联表
     */
    @PreAuthorize("@cc.check('system:menus:remove')")
    @Log("租户套餐菜单关联表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        sysOrgPackageMenusService.deleteSysOrgPackageMenusById(id);
        return R.ok();
    }
}
