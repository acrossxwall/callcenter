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
package cc.efit.modules.system.domain;

import cc.efit.data.permission.DataPermissionEntity;
import cc.efit.db.base.BaseCommonConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
* @date 2019-03-25
*/
@Entity
@Getter
@Setter
@Table(name="sys_dept")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class Dept extends DataPermissionEntity implements Serializable {

    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToMany(mappedBy = "depts")
    @Schema(name = "角色")
    private Set<Role> roles;

    @Schema(name = "排序")
    private Integer deptSort;

    @NotBlank
    @Schema(name = "部门名称")
    private String name;

    @NotNull
    @Schema(name = "是否启用 1-是 0-否")
    private Integer status;

    @Schema(name = "上级部门")
    private Integer pid;

    @Schema(name = "子节点数目", hidden = true)
    private Integer subCount = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dept dept = (Dept) o;
        return Objects.equals(id, dept.id) &&
                Objects.equals(name, dept.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}