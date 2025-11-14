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

/**
* 
* @date 2019-04-10
*/
@Getter
@Setter
public class DictDetailDto extends BaseDTO implements Serializable {

    @Schema(name = "ID")
    private Integer id;

    @Schema(name = "字典类型")
    private String dictType;

    @Schema(name = "字典标签")
    private String label;

    @Schema(name = "字典值")
    private String value;

    @Schema(name = "排序")
    private Integer dictSort;
    @Schema(name = "样式属性")
    private String cssClass;
    @Schema(name = "表格回显样式")
    private String listClass;

    private Integer convert;
}