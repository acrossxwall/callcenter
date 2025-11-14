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
package cc.efit.modules.maint.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.annotation.Log;
import cc.efit.modules.maint.domain.App;
import cc.efit.modules.maint.service.AppService;
import cc.efit.modules.maint.service.dto.AppDto;
import cc.efit.modules.maint.service.dto.AppQueryCriteria;
import cc.efit.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@RestController
@RequiredArgsConstructor
@Tag(name = "运维：应用管理")
@RequestMapping("/api/app")
public class AppController {

    private final AppService appService;

    @Operation(summary="导出应用数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('system:app:list')")
    public void exportApp(HttpServletResponse response, AppQueryCriteria criteria) throws IOException {
        appService.download(appService.queryAll(criteria), response);
    }

    @Operation(summary = "查询应用")
    @GetMapping
    @PreAuthorize("@cc.check('system:app:list')")
    public ResponseEntity<PageResult<AppDto>> queryApp(AppQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(appService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增应用")
    @Operation(summary = "新增应用")
    @PostMapping
    @PreAuthorize("@cc.check('system:app:add')")
    public ResponseEntity<Object> createApp(@Validated @RequestBody App resources){
        appService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改应用")
    @Operation(summary = "修改应用")
    @PutMapping
    @PreAuthorize("@cc.check('system:app:edit')")
    public ResponseEntity<Object> updateApp(@Validated @RequestBody App resources){
        appService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除应用")
    @Operation(summary = "删除应用")
    @DeleteMapping
    @PreAuthorize("@cc.check('system:app:remove')")
    public ResponseEntity<Object> deleteApp(@RequestBody Set<Integer> ids){
        appService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
