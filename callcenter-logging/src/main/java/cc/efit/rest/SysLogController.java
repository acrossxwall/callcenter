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
package cc.efit.rest;

import cc.efit.res.R;
import cc.efit.web.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.annotation.Log;
import cc.efit.service.SysLogService;
import cc.efit.service.dto.SysLogQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @date 2018-11-24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
@Tag(name = "系统：日志管理")
public class SysLogController {

    private final SysLogService sysLogService;

    @Log("导出数据")
    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('logs:export')")
    public void exportLog(HttpServletResponse response, SysLogQueryCriteria criteria) throws IOException {
        criteria.setLogType("INFO");
        sysLogService.download(sysLogService.queryAll(criteria), response);
    }

    @Log("导出错误数据")
    @Operation(summary="导出错误数据")
    @GetMapping(value = "/error/download")
    @PreAuthorize("@cc.check('logs:export')")
    public void exportErrorLog(HttpServletResponse response, SysLogQueryCriteria criteria) throws IOException {
        criteria.setLogType("ERROR");
        sysLogService.download(sysLogService.queryAll(criteria), response);
    }
    @GetMapping
    @Operation(summary="日志查询")
    @PreAuthorize("@cc.check('logs:list')")
    public R queryLog(SysLogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        return R.ok(sysLogService.queryAll(criteria,pageable));
    }

    @GetMapping(value = "/user")
    @Operation(summary="用户日志查询")
    public R queryUserLog(SysLogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        criteria.setUsername(SecurityUtils.getCurrentUsername());
        return R.ok(sysLogService.queryAllByUser(criteria,pageable));
    }

    @GetMapping(value = "/error")
    @Operation(summary="错误日志查询")
    @PreAuthorize("@cc.check()")
    public R queryErrorLog(SysLogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("ERROR");
        return R.ok(sysLogService.queryAll(criteria,pageable));
    }

    @GetMapping(value = "/error/{id}")
    @Operation(summary="日志异常详情查询")
    @PreAuthorize("@cc.check('logs:detail')")
    public R queryErrorLogDetail(@PathVariable Integer id){
        return R.ok(sysLogService.findByErrDetail(id));
    }
    @DeleteMapping(value = "/del/error")
    @Log("删除所有ERROR日志")
    @Operation(summary="删除所有ERROR日志")
    @PreAuthorize("@cc.check('logs:remove')")
    public R delAllErrorLog(){
        sysLogService.delAllByError();
        return R.ok(HttpStatus.OK);
    }

    @DeleteMapping(value = "/del/info")
    @Log("删除所有INFO日志")
    @Operation(summary="删除所有INFO日志")
    @PreAuthorize("@cc.check('logs:remove')")
    public R delAllInfoLog(){
        sysLogService.delAllByInfo();
        return R.ok(HttpStatus.OK);
    }
}
