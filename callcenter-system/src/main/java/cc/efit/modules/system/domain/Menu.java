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
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import cc.efit.db.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * @date 2018-12-17
 */
@Entity
@Getter
@Setter
@Table(name = "sys_menu")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class Menu extends BaseEntity implements Serializable {

    @Id
    @Column(name = "menu_id")
    @NotNull(groups = {Update.class})
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToMany(mappedBy = "menus")
    @Schema(name = "菜单角色")
    private Set<Role> roles;

    @Schema(name = "菜单标题")
    private String title;

    @Schema(name = "排序")
    private Integer menuSort = 999;

    @Schema(name = "组件路径")
    private String component;

    @Schema(name = "路由地址")
    private String path;

    @Schema(name = "菜单类型，0目录 1菜单 2按钮")
    private Integer type;

    @Schema(name = "权限标识")
    private String permission;

    @Schema(name = "菜单图标")
    private String icon;

    @Schema(name = "缓存 1-是 0-否")
    private Integer cache;

    @Schema(name = "上级菜单")
    private Integer pid;

    @Schema(name = "子节点数目", hidden = true)
    private Integer subCount = 0;

    @Schema(name = "外链菜单1-是 0-否")
    private Integer frame;
    @Schema(name = "状态 1-是 0-否")
    private Integer status;
    /** 显示状态（1显示 0隐藏） */
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
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
