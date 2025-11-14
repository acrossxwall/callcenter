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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.annotation.Log;
import cc.efit.exception.BadRequestException;
import cc.efit.modules.system.domain.Job;
import cc.efit.modules.system.service.JobService;
import cc.efit.modules.system.service.dto.JobQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
*
* @date 2019-03-29
*/
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：岗位管理")
@RequestMapping("/api/job")
public class JobController {

    private final JobService jobService;
    private static final String ENTITY_NAME = "job";

    @Operation(summary="导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('system:job:list')")
    public void exportJob(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        jobService.download(jobService.queryAll(criteria), response);
    }

    @Operation(summary="查询岗位")
    @GetMapping
    @PreAuthorize("@cc.check('system:job:list','system:user:list')")
    public R queryJob(JobQueryCriteria criteria, Pageable pageable){
        return R.ok(jobService.queryAll(criteria, pageable));
    }

    @Operation(summary="查询启用岗位")
    @GetMapping("/all")
    @PreAuthorize("@cc.check('system:job:list','system:user:list')")
    public R queryEnableJob( ){
        return R.ok(jobService.queryEnableAllJob( ));
    }

    @Log("新增岗位")
    @Operation(summary="新增岗位")
    @PostMapping
    @PreAuthorize("@cc.check('system:job:add')")
    public R createJob(@Validated @RequestBody Job resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        jobService.create(resources);
        return R.ok();
    }

    @Log("修改岗位")
    @Operation(summary="修改岗位")
    @PutMapping
    @PreAuthorize("@cc.check('system:job:edit')")
    public R updateJob(@Validated(Job.Update.class) @RequestBody Job resources){
        jobService.update(resources);
        return R.ok();
    }

    @Log("删除岗位")
    @Operation(summary="删除岗位")
    @DeleteMapping
    @PreAuthorize("@cc.check('system:job:remove')")
    public R deleteJob(@RequestBody Set<Integer> ids){
        // 验证是否被用户关联
        jobService.verification(ids);
        jobService.delete(ids);
        return R.ok();
    }
    @Log("删除岗位")
    @Operation(summary="删除岗位")
    @DeleteMapping("{id}")
    @PreAuthorize("@cc.check('system:job:remove')")
    public R deleteJobById(@PathVariable("id") Integer id){
        // 验证是否被用户关联
        Set<Integer> set = Set.of(id);
        jobService.verification(set);
        jobService.delete(set);
        return R.ok();
    }

    @Log("查询岗位详情")
    @Operation(summary="查询岗位详情")
    @GetMapping("{id}")
    @PreAuthorize("@cc.check('system:job:list')")
    public R getJobById(@PathVariable("id") Integer id){
        return R.ok(jobService.findById(id));
    }

}