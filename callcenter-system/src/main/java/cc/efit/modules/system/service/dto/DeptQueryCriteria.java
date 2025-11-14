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
* @date 2019-03-25
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class DeptQueryCriteria  extends BaseQueryReq {

    @Schema(name = "名称")
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    @Schema(name = "是否启用")
    private Integer status;

    @Query
    @Schema(name = "上级部门")
    private Integer pid;

    @Schema(name = "PID空查询", hidden = true)
    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @Schema(name = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}