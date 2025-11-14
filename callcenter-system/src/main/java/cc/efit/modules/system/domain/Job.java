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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.util.Objects;

/**
*
* @date 2019-03-29
*/
@Entity
@Getter
@Setter
@Table(name="sys_job")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class Job extends DataPermissionEntity implements Serializable {

    @Id
    @Column(name = "job_id")
    @NotNull(groups = Update.class)
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Schema(name = "岗位名称")
    private String name;

    @NotNull
    @Schema(name = "岗位排序")
    private Integer jobSort;

    @NotNull
    @Schema(name = "是否启用 1-启用 0-禁用")
    private Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}