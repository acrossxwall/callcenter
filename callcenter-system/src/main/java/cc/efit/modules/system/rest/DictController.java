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
import cc.efit.modules.system.domain.Dict;
import cc.efit.modules.system.service.DictService;
import cc.efit.modules.system.service.dto.DictQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
*
* @date 2019-04-10
*/
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：字典管理")
@RequestMapping("/api/dict")
public class DictController {

    private final DictService dictService;
    private static final String ENTITY_NAME = "dict";

    @Operation(summary="导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('system:dict:list')")
    public void exportDict(HttpServletResponse response, DictQueryCriteria criteria) throws IOException {
        dictService.download(dictService.queryAll(criteria), response);
    }

    @Operation(summary="查询字典")
    @GetMapping(value = "/all")
    @PreAuthorize("@cc.check('system:dict:list')")
    public R queryAllDict(){
        return R.ok(dictService.queryAll(new DictQueryCriteria()));
    }

    @Operation(summary="查询字典")
    @GetMapping
    @PreAuthorize("@cc.check('system:dict:list')")
    public R queryDict(DictQueryCriteria resources, Pageable pageable){
        return R.ok(dictService.queryAll(resources,pageable));
    }

    @Operation(summary="获取单个dict")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@cc.check('system:dict:list')")
    public R findDictById(@PathVariable Integer id){
        return R.ok(dictService.findById(id));
    }

    @Log("新增字典")
    @Operation(summary="新增字典")
    @PostMapping
    @PreAuthorize("@cc.check('system:dict:add')")
    public R createDict(@Validated @RequestBody Dict resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        dictService.create(resources);
        return R.ok();
    }

    @Log("修改字典")
    @Operation(summary="修改字典")
    @PutMapping
    @PreAuthorize("@cc.check('system:dict:edit')")
    public R updateDict(@Validated(Dict.Update.class) @RequestBody Dict resources){
        dictService.update(resources);
        return R.ok();
    }

    @Log("删除字典")
    @Operation(summary="删除字典")
    @DeleteMapping
    @PreAuthorize("@cc.check('system:dict:remove')")
    public R deleteDict(@RequestBody Set<Integer> ids){
        dictService.delete(ids);
        return R.ok();
    }
}