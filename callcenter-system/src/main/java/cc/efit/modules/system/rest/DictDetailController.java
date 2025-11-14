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
import cc.efit.modules.system.domain.DictDetail;
import cc.efit.modules.system.service.DictDetailService;
import cc.efit.modules.system.service.dto.DictDetailDto;
import cc.efit.modules.system.service.dto.DictDetailQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 
* @date 2019-04-10
*/
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
public class DictDetailController {

    private final DictDetailService dictDetailService;
    private static final String ENTITY_NAME = "dictDetail";

    @Operation(summary="查询字典详情")
    @GetMapping
    @PreAuthorize("@cc.check('system:dict:list')")
    public R queryDictDetail(DictDetailQueryCriteria criteria, @PageableDefault(sort = {"dictSort"}, direction = Sort.Direction.ASC) Pageable pageable){
        return R.ok(dictDetailService.queryAll(criteria,pageable));
    }

    @Operation(summary="查询多个字典详情")
    @GetMapping(value = "/map")
    public R getDictDetailMaps(@RequestParam String dictName){
        String[] names = dictName.split("[,，]");
        Map<String, List<DictDetailDto>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, dictDetailService.getDictByName(name));
        }
        return R.ok(dictMap);
    }

    @Log("新增字典详情")
    @Operation(summary="新增字典详情")
    @PostMapping
    @PreAuthorize("@cc.check('system:dict:add')")
    public R createDictDetail(@Validated @RequestBody DictDetail resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        dictDetailService.create(resources);
        return R.ok();
    }

    @Log("修改字典详情")
    @Operation(summary="修改字典详情")
    @PutMapping
    @PreAuthorize("@cc.check('system:dict:edit')")
    public R updateDictDetail(@Validated(DictDetail.Update.class) @RequestBody DictDetail resources){
        dictDetailService.update(resources);
        return R.ok();
    }

    @Operation(summary="查询字典详情")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@cc.check('system:dict:list')")
    public R getDictDetail(@PathVariable Integer id){
        return R.ok(dictDetailService.findById(id));
    }

    @Operation(summary="查询字典内容详情")
    @GetMapping(value = "/type/{dictType}")
    public R getDictDetail(@PathVariable String dictType){
        return R.ok(dictDetailService.getDictByName(dictType));
    }

    @Log("删除字典详情")
    @Operation(summary="删除字典详情")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@cc.check('system:dict:remove')")
    public R deleteDictDetail(@PathVariable Integer id){
        dictDetailService.delete(id);
        return R.ok();
    }
}