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
package cc.efit.modules.system.repository;

import cc.efit.db.base.LogicDeletedRepository;
import cc.efit.modules.system.domain.Menu;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @date 2018-12-17
 */
public interface MenuRepository extends LogicDeletedRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {

    /**
     * 根据菜单标题查询
     * @param title 菜单标题
     * @return /
     */
    Menu findByTitle(String title);

    /**
     * 根据组件名称查询
     * @param name 组件名称
     * @return /
     */
    Menu findByRouteName(String name);

    /**
     * 根据菜单的 PID 查询
     * @param pid /
     * @return /
     */
    List<Menu> findByPidOrderByMenuSort(Integer pid);

    /**
     * 查询顶级菜单
     * @return /
     */
    List<Menu> findByPidIsNullOrderByMenuSort();

    List<Menu> findByPidIsNullAndStatusOrderByMenuSort(Integer status);

    List<Menu> findByPidAndStatusOrderByMenuSort(Integer pid, Integer status);


    /**
     * 根据角色ID与菜单类型查询菜单
     * @param roleIds roleIDs
     * @param type 类型
     * @return /
     */
    @Query(value = """
            SELECT m.* FROM sys_menu m, sys_roles_menus r WHERE
             m.menu_id = r.menu_id AND r.role_id IN ?1 AND type != ?2 order by m.menu_sort asc
             """, nativeQuery = true)
    LinkedHashSet<Menu> findByRoleIdsAndTypeNot(Set<Integer> roleIds, int type);

    /**
     * 获取节点数量
     * @param id /
     * @return /
     */
    int countByPid(Integer id);

    /**
     * 更新节点数目
     * @param count /
     * @param menuId /
     */
    @Modifying
    @Query(value = " update Menu set subCount = ?1 where  id = ?2 " )
    void updateSubCntById(int count, Integer menuId);

    List<Menu> findByPidIsNullAndStatusAndIdInOrderByMenuSort(Integer status, List<Integer> packageMenuIds);
}
