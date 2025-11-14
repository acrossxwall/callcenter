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
package cc.efit.modules.security.rest;

import cc.efit.res.R;
import cc.efit.utils.EncryptUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import cc.efit.modules.security.service.OnlineUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/online")
@Tag(name = "系统：在线用户管理")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    @Operation(summary="查询在线用户")
    @GetMapping
    @PreAuthorize("@cc.check()")
    public  R queryOnlineUser(String username, Pageable pageable){
        return R.ok(onlineUserService.getAll(username, pageable) );
    }

    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@cc.check()")
    public void exportOnlineUser(HttpServletResponse response, String username) throws IOException {
        onlineUserService.download(onlineUserService.getAll(username), response);
    }

    @Operation(summary="踢出用户")
    @DeleteMapping("/{key}")
    @PreAuthorize("@cc.check()")
    public R deleteOnlineUser(@PathVariable("key")String key) throws Exception {
            // 解密Key
        onlineUserService.logout(EncryptUtils.desDecrypt(key));
        return R.ok();
    }
}
