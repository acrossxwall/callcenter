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
package cc.efit.service.dto;

import cc.efit.db.annotation.Query;
import cc.efit.db.base.BaseQueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

/**
 * 日志查询类
 *
 * @date 2019-6-4 09:23:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLogQueryCriteria extends BaseQueryReq {

    @Schema(name = "模糊查询")
    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;

    @Query
    @Schema(name = "用户名")
    private String username;

    @Query
    @Schema(name = "日志类型")
    private String logType;

    @Schema(name = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
