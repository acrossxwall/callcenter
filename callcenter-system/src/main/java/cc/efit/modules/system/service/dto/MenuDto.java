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
import java.util.List;
import java.util.Objects;

/**
 * 
 * @date 2018-12-17
 */
@Getter
@Setter
public class MenuDto extends BaseDTO implements Serializable {

    @Schema(name = "ID")
    private Integer id;

    @Schema(name = "子节点")
    private List<MenuDto> children;

    @Schema(name = "类型")
    private Integer type;

    @Schema(name = "权限")
    private String permission;

    @Schema(name = "菜单标题")
    private String title;

    @Schema(name = "排序")
    private Integer menuSort;

    @Schema(name = "路径")
    private String path;

    @Schema(name = "组件")
    private String component;

    @Schema(name = "PID")
    private Integer pid;

    @Schema(name = "子节点数目")
    private Integer subCount;

    @Schema(name = "是否缓存 1-是 0-否")
    private Integer cache;

    @Schema(name = "图标")
    private String icon;
    @Schema(name = "外链菜单1-是 0-否")
    private Integer frame;
    @Schema(name = "状态 1-是 0-否")
    private Integer status;
    /** 显示状态（1显示 0隐藏） */
    @Schema(name = "显示状态（1显示 0隐藏）")
    private Integer visible;

    /** 路由参数 */
    private String query;

    /** 路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义） */
    private String routeName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(id, menuDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
