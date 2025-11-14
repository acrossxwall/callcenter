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
import cc.efit.service.GeneratorService;
import cc.efit.service.dto.CodeConfigQueryCriteria;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.domain.CodeConfig;
import cc.efit.service.CodeConfigService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code/config")
@Tag(name = "系统：代码生成器配置管理")
public class CodeConfigController {

    private final CodeConfigService genConfigService;
    private final GeneratorService generatorService;
    @Operation(summary="查询ById")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@cc.check('code:gen:query')")
    public R queryCodeConfig(@PathVariable("id") Integer id){
        return R.ok(generatorService.findCodeConfigInfo(id) );
    }

    @Operation(summary="查询")
    @GetMapping(value = "/list")
    @PreAuthorize("@cc.check('code:gen:query')")
    public R queryCodeConfigList(CodeConfigQueryCriteria criteria, Pageable pageable){
        return R.ok(genConfigService.queryAll(criteria, pageable) );
    }

    @PutMapping
    @Operation(summary="修改")
    @PreAuthorize("@cc.check('code:gen:edit')")
    public R updateGenConfig(@Validated @RequestBody CodeConfig genConfig){
        genConfigService.update(genConfig);
        return R.ok();
    }

    @Operation(summary="删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@cc.check('code:gen:remove')")
    public R deleteCodeConfigById(@PathVariable Integer id){
        genConfigService.deleteById(id);
        return R.ok();
    }
}
