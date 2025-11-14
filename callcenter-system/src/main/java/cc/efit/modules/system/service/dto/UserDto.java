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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @date 2018-11-23
 */
@Getter
@Setter
public class UserDto extends BaseDTO implements Serializable {

    @Schema(name = "ID")
    private Integer id;

    @Schema(name = "角色")
    private Set<RoleSmallDto> roles;

    @Schema(name = "岗位")
    private Set<JobSmallDto> jobs;

    @Schema(name = "部门")
    private DeptSmallDto dept;

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "昵称")
    private String nickName;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "电话")
    private String phone;

    @Schema(name = "性别")
    private String gender;

    @Schema(name = "头像")
    private String avatarName;

    @Schema(name = "头像路径")
    private String avatarPath;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "是否启用")
    private Integer status;

    @Schema(name = "管理员")
    private Integer userType;

    @Schema(name = "最后登录IP" )
    private String loginIp;

    /** 最后登录时间 */
    @Schema(name = "最后登录时间")
    private Date loginDate;

    @Schema(name = "密码重置时间")
    private Date pwdUpdateTime;

    private Integer orgId;
}
