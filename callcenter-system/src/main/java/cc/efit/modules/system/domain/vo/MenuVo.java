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
package cc.efit.modules.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 构建前端路由时用到
 * 
 * @date 2018-12-20
 */
@Data
public class MenuVo implements Serializable {

    @Schema(name = "菜单名称")
    private String name;

    @Schema(name = "路径")
    private String path;

    @Schema(name = "隐藏状态")
    private Boolean hidden;

    @Schema(name = "重定向")
    private String redirect;

    @Schema(name = "组件")
    private String component;

    @Schema(name = "总是显示")
    private Boolean alwaysShow;

    @Schema(name = "元数据")
    private MenuMetaVo meta;

    @Schema(name = "子路由")
    private List<MenuVo> children;
}
