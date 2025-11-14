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

import cc.efit.db.base.BaseCommonConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import cc.efit.db.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
* 
* @date 2019-04-10
*/
@Entity
@Getter
@Setter
@Table(name="sys_dict_detail")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class DictDetail extends BaseEntity implements Serializable {

    @Id
    @Column(name = "detail_id")
    @NotNull(groups = Update.class)
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(name = "字典类型", hidden = true)
    private String dictType;

    @Schema(name = "字典标签")
    private String label;

    @Schema(name = "字典值")
    private String value;

    @Schema(name = "排序")
    private Integer dictSort = 999;
    /**
     * 是否将字典的字符串的数字转化为数值类型，
     * 例如：1-是，0-否，如果convert为1，则value为1时，返回的值为true
     * el-radio 对于数字和字符串比较不对，而我又喜欢状态使用数字，没办法喽
     */
    private Integer convert;

    private String cssClass;

    private String listClass;
}