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
package cc.efit.service;

import cc.efit.domain.CodeConfig;
import cc.efit.service.dto.CodeConfigQueryCriteria;
import cc.efit.utils.PageResult;
import org.springframework.data.domain.Pageable;

public interface CodeConfigService {

    /**
     * 查询表配置
     * @param tableName 表名
     * @return 表配置
     */
    CodeConfig find(String tableName);

    /**
     * 更新表配置
     * @param genConfig 表配置
     * @return 表配置
     */
    CodeConfig update(CodeConfig genConfig);

    PageResult<CodeConfig> queryAll(CodeConfigQueryCriteria criteria, Pageable pageable);

    void deleteById(Integer id);
}
