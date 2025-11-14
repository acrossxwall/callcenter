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
package cc.efit.modules.system.service.dto;

import cc.efit.db.base.BaseQueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import cc.efit.db.annotation.Query;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * 公共查询类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryCriteria extends BaseQueryReq {

    @Schema(name = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
    @Query
    private Integer status;
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(name = "角色名称")
    private String name;
    @Schema(name = "权限标识")
    @Query(type = Query.Type.INNER_LIKE)
    private String roleKey;
}
