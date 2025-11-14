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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.domain.ColumnInfo;
import cc.efit.exception.BadRequestException;
import cc.efit.service.CodeConfigService;
import cc.efit.service.GeneratorService;
import cc.efit.utils.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/generator")
@Tag(name = "系统：代码生成管理")
public class GeneratorController {

    private final GeneratorService generatorService;
    private final CodeConfigService genConfigService;

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    @Operation(summary="查询数据库数据")
    @GetMapping(value = "/tables")
    @PreAuthorize("@cc.check('code:gen:query')")
    public R queryTables(@RequestParam(defaultValue = "") String tableName,
                                                             @RequestParam(defaultValue = "1")Integer pageNum,
                                                             @RequestParam(defaultValue = "10")Integer pageSize){
        if( pageNum < 1){
            pageNum=1;
        }
        int[] startEnd = PageUtil.transToStartEnd(pageNum-1, pageSize);
        return R.ok(generatorService.getTables(tableName,startEnd) );
    }

    @Operation(summary="查询字段数据")
    @GetMapping(value = "/columns")
    @PreAuthorize("@cc.check('code:gen:query')")
    public R queryColumns(@RequestParam String tableName){
        List<ColumnInfo> columnInfos = generatorService.getColumns(tableName);
        return R.ok(PageUtil.toPage(columnInfos,columnInfos.size()) );
    }

    @Operation(summary="同步字段数据")
    @PostMapping(value = "/sync")
    @PreAuthorize("@cc.check('code:gen:query')")
    public R syncColumn(@RequestParam String tables){
        generatorService.sync(Arrays.asList(tables.split(",")),false);
        return R.ok();
    }

    @Operation(summary="强制同步字段数据")
    @PostMapping(value = "/forceSync")
    @PreAuthorize("@cc.check('code:gen:query')")
    public R forceSyncColumn(@RequestParam String tables){
        generatorService.sync(Arrays.asList(tables.split(",")),true);
        return R.ok();
    }

    @Operation(summary="生成代码")
    @GetMapping(value = "/genCode/{tableName}")
    @PreAuthorize("@cc.check('code:gen:edit')")
    public R generatorCode(@PathVariable("tableName") String tableName){
        if(!generatorEnabled  ){
            throw new BadRequestException("此环境不允许生成代码，请选择预览或者下载查看！");
        }
        generatorService.generator(tableName);
        return R.ok();
    }
}
