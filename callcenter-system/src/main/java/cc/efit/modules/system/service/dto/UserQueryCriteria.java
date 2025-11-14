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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @date 2018-11-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryCriteria extends BaseQueryReq implements Serializable {

    @Query
    @Schema(name = "ID")
    private Integer id;

    @Schema(name = "部门ID集合")
    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Integer> deptIds = new HashSet<>();

    @Schema(name = "模糊查询")
    @Query(blurry = "email,username,nickName")
    private String blurry;

    @Query
    @Schema(name = "是否启用")
    private Integer status;

    @Schema(name = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
    @Query
    private String username;
    @Query
    private String phone;
    @Query(propName = "id", type = Query.Type.EQUAL, joinName = "roles" ,join = Query.Join.INNER)
    private Integer roleId;
    @Query(propName = "id", type = Query.Type.NOT_EQUAL_OR_NULL, joinName = "roles" ,join = Query.Join.LEFT)
    private Integer notRoleId;
    @Query
    private Integer userType;

    private Integer userDeptId;
}
