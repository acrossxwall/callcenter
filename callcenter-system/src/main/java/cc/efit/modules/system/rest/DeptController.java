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

import cc.efit.res.R;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.annotation.Log;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.system.domain.Dept;
import cc.efit.modules.system.service.DeptService;
import cc.efit.modules.system.service.dto.DeptDto;
import cc.efit.modules.system.service.dto.DeptQueryCriteria;
import cc.efit.utils.PageUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

/**
*
* @date 2019-03-25
*/
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：部门管理")
@RequestMapping("/api/dept")
public class DeptController {

    private final DeptService deptService;
    private static final String ENTITY_NAME = "dept";

    @Operation(summary="导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('system:dept:list')")
    public void exportDept(HttpServletResponse response, DeptQueryCriteria criteria) throws Exception {
        deptService.download(deptService.queryAll(criteria, false), response);
    }

    @Operation(summary="查询部门")
    @GetMapping("/list")
    @PreAuthorize("@cc.check('system:user:list','system:dept:list')")
    public R queryDept(DeptQueryCriteria criteria) throws Exception {
        List<DeptDto> depts = deptService.queryAll(criteria, true);
        return R.ok(PageUtil.toPage(depts, depts.size()) );
    }

    @Operation(summary="查询部门")
    @GetMapping("/superior")
    @PreAuthorize("@cc.check('system:user:list','system:dept:list')")
    public R getDeptSuperior(@RequestParam(value = "pid", required = false) Integer id) {
        return R.ok(deptService.buildTree(id) );
    }

    @Log("新增部门")
    @Operation(summary="新增部门")
    @PostMapping
    @PreAuthorize("@cc.check('system:dept:add')")
    public R  createDept(@Validated @RequestBody Dept resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        deptService.create(resources);
        return R.ok( );
    }

    @Log("修改部门")
    @Operation(summary="修改部门")
    @PutMapping
    @PreAuthorize("@cc.check('system:dept:edit')")
    public R updateDept(@Validated(Dept.Update.class) @RequestBody Dept resources){
        deptService.update(resources);
        return R.ok( );
    }

    @Log("删除部门")
    @Operation(summary="删除部门")
    @DeleteMapping
    @PreAuthorize("@cc.check('system:dept:remove')")
    public R deleteDept(@RequestBody Set<Integer> ids){
        Set<DeptDto> deptDtos = new HashSet<>();
        for (Integer id : ids) {
            List<Dept> deptList = deptService.findByPid(id);
            deptDtos.add(deptService.findById(id));
            if(CollectionUtil.isNotEmpty(deptList)){
                deptDtos = deptService.getDeleteDepts(deptList, deptDtos);
            }
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptDtos);
        deptService.delete(deptDtos);
        return R.ok();
    }

    @Log("查询部门")
    @Operation(summary="查询部门")
    @GetMapping("/{id}")
    @PreAuthorize("@cc.check('system:dept:list')")
    public R getDeptById(@PathVariable("id")Integer id){
        return R.ok(deptService.findById(id));
    }

    @Log("删除部门")
    @Operation(summary="删除部门")
    @DeleteMapping("/{id}")
    @PreAuthorize("@cc.check('system:dept:remove')")
    public R deleteDeptById(@PathVariable("id")Integer id){
        Set<DeptDto> deptDtos = new HashSet<>();
        List<Dept> deptList = deptService.findByPid(id);
        deptDtos.add(deptService.findById(id));
        if(CollectionUtil.isNotEmpty(deptList)){
            deptDtos = deptService.getDeleteDepts(deptList, deptDtos);
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptDtos);
        deptService.delete(deptDtos);
        return R.ok();
    }
}