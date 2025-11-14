package cc.efit.system.rest;

import cc.efit.res.R;
import java.util.List;

import cc.efit.system.req.SysOrgPackageReq;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import cc.efit.system.domain.SysOrgPackage;
import cc.efit.system.service.SysOrgPackageService;
import cc.efit.system.service.dto.SysOrgPackageQueryCriteria;

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
 * 租户套餐表Controller
 * 
 * @author across
 * @date 2025-10-28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/org/package")
public class SysOrgPackageController {

    private final SysOrgPackageService sysOrgPackageService;

    /**
     * 查询租户套餐表列表
     */
    @PreAuthorize("@cc.check('org:package:list')")
    @GetMapping("/list")
    public R list(SysOrgPackageQueryCriteria criteria, Pageable pageable) {
        return R.ok(sysOrgPackageService.queryAll(criteria,pageable));
    }

    /**
     * 导出租户套餐表列表
     */
    @PreAuthorize("@cc.check('org:package:export')")
    @Log("租户套餐表导出" )
    @PostMapping("/export")
    public void exportSysOrgPackage(HttpServletResponse response, SysOrgPackageQueryCriteria criteria) throws IOException {
        sysOrgPackageService.download(sysOrgPackageService.queryAll(criteria), response);
    }

    /**
     * 获取租户套餐表详细信息
     */
    @PreAuthorize("@cc.check('org:package:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Integer id) {
        return R.ok(sysOrgPackageService.selectSysOrgPackageById(id));
    }

    /**
     * 新增租户套餐表
     */
    @PreAuthorize("@cc.check('org:package:add')")
    @Log("租户套餐表新增" )
    @PostMapping
    public R add(@RequestBody SysOrgPackageReq req) {
        sysOrgPackageService.insertSysOrgPackage(req);
        return R.ok();
    }

    /**
     * 修改租户套餐表
     */
    @PreAuthorize("@cc.check('org:package:edit')")
    @Log("租户套餐表修改" )
    @PutMapping
    public R edit(@RequestBody SysOrgPackageReq sysOrgPackage) {
        sysOrgPackageService.updateSysOrgPackage(sysOrgPackage);
        return R.ok();
    }

    /**
     * 删除租户套餐表
     */
    @PreAuthorize("@cc.check('org:package:remove')")
    @Log("租户套餐表删除" )
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer  id ) {
        sysOrgPackageService.deleteSysOrgPackageById(id);
        return R.ok();
    }
    @GetMapping(value = "/enable/list")
    public R getEnablePackageList() {
        return R.ok(sysOrgPackageService.getEnablePackageList());
    }
}
