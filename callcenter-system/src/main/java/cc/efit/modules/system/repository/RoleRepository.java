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
import cc.efit.modules.system.domain.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 
 * @date 2018-12-03
 */
public interface RoleRepository extends LogicDeletedRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    /**
     * 根据名称查询
     * @param name /
     * @return /
     */
    Role findByName(String name);


    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    @Query(value = """
            SELECT r.* FROM sys_role r, sys_users_roles u WHERE
             r.role_id = u.role_id AND u.user_id = ?1
            """,nativeQuery = true)
    Set<Role> findByUserId(Integer id);

    /**
     * 解绑角色菜单
     * @param id 菜单ID
     */
    @Modifying
    @Query(value = "delete from sys_roles_menus where menu_id = ?1",nativeQuery = true)
    void untiedMenu(Integer id);

    @Modifying
    @Query(value = "delete from sys_roles_menus where menu_id = ?1 and org_id=?2 ",nativeQuery = true)
    void untiedMenu(Integer id, Integer orgId);

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
    @Query(value = """
             select count(1) from sys_role r, sys_roles_depts d where
              r.role_id = d.role_id and d.dept_id in ?1
             """,nativeQuery = true)
    int countByDepts(Set<Integer> deptIds);

    @Query(value = """
             SELECT m.role_id from sys_roles_menus m WHERE  m.menu_id in ?1 and m.org_id=?2
             """ , nativeQuery = true)
    List<Integer> findRoleIdByMenuIdsAndOrgId(Set<Integer> menuIds, Integer orgId);

    /**
     * 根据菜单Id查询
     * @param menuIds /
     * @return /
     */
    @Query(value = """
             SELECT r.* FROM sys_role r, sys_roles_menus m WHERE
              r.role_id = m.role_id AND m.menu_id in ?1
             """ ,nativeQuery = true)
    List<Role> findInMenuId(List<Integer> menuIds);

    List<Role> findByStatus(Integer status);

    @Query("UPDATE Role e SET e.status = ?2 WHERE e.id = ?1")
    @Modifying
    @Transactional
    void updateRoleStatus(Integer id,Integer status);

    @Modifying
    @Query(value = """
            INSERT INTO sys_users_roles (user_id, role_id, org_id) VALUES (?1, ?2,?3)
            """, nativeQuery = true)
    void insertUserRole(Integer userId,Integer roleId,Integer orgId);

    @Modifying
    @Query(value = """
            INSERT INTO sys_roles_menus (menu_id, role_id, org_id) VALUES (?1, ?2,?3)
            """, nativeQuery = true)
    void insertRoleMenu(Integer menuId,Integer roleId,Integer orgId);

    @Query(value = "SELECT id FROM Role WHERE orgId = ?1 AND roleKey = 'org_admin'")
    Integer findOrgAdminRoleByOrgId(Integer orgId);
}
