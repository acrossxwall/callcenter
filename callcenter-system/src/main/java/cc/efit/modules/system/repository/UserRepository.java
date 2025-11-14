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
import cc.efit.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @date 2018-11-22
 */
public interface UserRepository extends LogicDeletedRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return /
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询
     * @param email 邮箱
     * @return /
     */
    User findByEmail(String email);

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return /
     */
    User findByPhone(String phone);

    /**
     * 修改密码
     * @param username 用户名
     * @param pass 密码
     * @param lastPasswordResetTime /
     */
    @Modifying
    @Query(value = "update User set password = ?2 , pwdUpdateTime = ?3 where username = ?1")
    void updatePass(String username, String pass, Date lastPasswordResetTime);

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    @Modifying
    @Query(value = "update User set email = ?2 where username = ?1" )
    void updateEmail(String username, String email);

    /**
     * 根据角色查询用户
     * @param roleId /
     * @return /
     */
    @Query(value = """
            SELECT u.* FROM sys_user u, sys_users_roles r WHERE
             u.id = r.user_id AND r.role_id = ?1
            """, nativeQuery = true)
    List<User> findByRoleId(Integer roleId);

    /**
     * 根据角色中的部门查询
     * @param deptId /
     * @return /
     */
    @Query(value = """
            SELECT u.* FROM sys_user u, sys_users_roles r, sys_roles_depts d WHERE
             u.id = r.user_id AND r.role_id = d.role_id AND d.dept_id = ?1 group by u.id
            """, nativeQuery = true)
    List<User> findByRoleDeptId(Integer deptId);

    /**
     * 根据菜单查询
     * @param id 菜单ID
     * @return /
     */
    @Query(value = """
            SELECT u.* FROM sys_user u, sys_users_roles ur, sys_roles_menus rm WHERE
             u.id = ur.user_id AND ur.role_id = rm.role_id AND rm.menu_id = ?1 group by u.id
            """, nativeQuery = true)
    List<User> findByMenuId(Integer id);


    /**
     * 根据岗位查询
     * @param ids /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_jobs j WHERE u.id = j.user_id AND j.job_id IN ?1", nativeQuery = true)
    int countByJobs(Set<Integer> ids);

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.user_dept_id IN ?1", nativeQuery = true)
    int countByDepts(Set<Integer> deptIds);

    /**
     * 根据角色查询
     * @param ids /
     * @return /
     */
    @Query(value = """
            SELECT count(1) FROM sys_user u, sys_users_roles r WHERE
             u.id = r.user_id AND r.role_id in ?1
            """, nativeQuery = true)
    int countByRoles(Set<Integer> ids);

    /**
     * 重置密码
     * @param ids 、
     * @param pwd 、
     */
    @Modifying
    @Query(value = "update User set password = ?2 where  id in ?1" )
    void resetPwd(Set<Integer> ids, String pwd);
    /**
     * 更新登录信息，最近登录时间、最近登录IP
     * @param username  用户名
     * @param loginIp   登录ip
     * @param loginDate 登录时间
     */
    @Modifying
    @Query(value = "update User set loginIp = ?2,loginDate=?3 where  username = ?1" )
    void updateUserLoginInfo(String username, String loginIp, Date loginDate);

    @Query("UPDATE User u SET u.status = ?2 WHERE u.id = ?1")
    @Modifying
    @Transactional
    void updateUserStatus(Integer id,Integer status);

    @Query("UPDATE User u SET u.status = ?2 WHERE u.orgId = ?1")
    @Modifying
    @Transactional
    void updateUserStatusByOrgId(Integer orgId,Integer status);

    /**
     * 更新用户中心信息（昵称、手机、性别、邮箱）
     * @param id 用户ID
     * @param nickName 昵称
     * @param phone 手机号
     * @param gender 性别
     * @param email 邮箱
     */
    @Modifying
    @Query("""
           UPDATE User u SET
           u.nickName = ?2, u.phone = ?3, u.gender = ?4, u.email = ?5
           WHERE u.id = ?1
           """)
    void updateCenterInfo(Integer id, String nickName, String phone, String gender, String email);

    List<User> findByOrgId(Integer orgId);
    @Modifying
    @Query("""
           UPDATE User u SET
           u.avatarName = ?2, u.avatarPath = ?3
           WHERE u.id = ?1
           """)
    void updateUserAvatarInfo(Integer id, String avatarName, String avatarPath);
    @Query("SELECT  u.avatarPath FROM User u WHERE u.id = ?1")
    String selectOldUserAvatarInfo(Integer id);

}
