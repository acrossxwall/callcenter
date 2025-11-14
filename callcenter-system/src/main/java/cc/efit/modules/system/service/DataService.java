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
package cc.efit.modules.system.service;

import cc.efit.modules.system.service.dto.UserDto;
import cc.efit.web.base.UserDataScope;

/**
 * 数据权限服务类
 */
public interface DataService {

    /**
     * 获取数据权限
     * @param user /
     * @return /
     */
    UserDataScope getDeptIds(UserDto user);
}
