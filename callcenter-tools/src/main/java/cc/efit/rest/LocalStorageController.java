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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import cc.efit.annotation.Log;
import cc.efit.domain.LocalStorage;
import cc.efit.exception.BadRequestException;
import cc.efit.service.LocalStorageService;
import cc.efit.service.dto.LocalStorageQueryCriteria;
import cc.efit.utils.FileUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* 
* @date 2019-09-05
*/
@RestController
@RequiredArgsConstructor
@Tag(name = "工具：本地存储管理")
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;

    @GetMapping
    @Operation(summary="查询文件")
    @PreAuthorize("@cc.check('storage:list')")
    public R queryFile(LocalStorageQueryCriteria criteria, Pageable pageable){
        return R.ok(localStorageService.queryAll(criteria,pageable));
    }

    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check('storage:list')")
    public void exportFile(HttpServletResponse response, LocalStorageQueryCriteria criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }

    @PostMapping( consumes = "multipart/form-data")
    @Operation(summary="上传文件")
    @PreAuthorize("@cc.check('storage:add')")
    public R  createFile(@RequestParam(required = false, value = "name") String name, @RequestParam("file") MultipartFile file){
        LocalStorage localStorage = localStorageService.create(name, file);
        return R.ok(localStorage.getId());
    }

    @Operation(summary="上传图片")
    @PostMapping("/pictures")
    public R  uploadPicture(@RequestParam MultipartFile file){
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if(!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))){
            throw new BadRequestException("只能上传图片");
        }
        LocalStorage localStorage = localStorageService.create(null, file);
        return R.ok(localStorage);
    }

    @PutMapping
    @Log("修改文件")
    @Operation(summary="修改文件")
    @PreAuthorize("@cc.check('storage:edit')")
    public R updateFile(@Validated @RequestBody LocalStorage resources){
        localStorageService.update(resources);
        return R.ok();
    }

    @Log("删除文件")
    @DeleteMapping
    @Operation(summary="多选删除")
    public R  deleteFile(@RequestBody Integer[] ids) {
        localStorageService.deleteAll(ids);
        return R.ok();
    }
}