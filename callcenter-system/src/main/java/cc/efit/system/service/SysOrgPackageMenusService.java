package cc.efit.system.service;

import java.util.List;
import cc.efit.system.domain.SysOrgPackageMenus;
import cc.efit.system.service.dto.SysOrgPackageMenusDto;
import cc.efit.system.service.dto.SysOrgPackageMenusQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 租户套餐菜单关联表Service接口
 * 
 * @author across
 * @date 2025-10-28
 */
public interface SysOrgPackageMenusService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<SysOrgPackageMenusDto> queryAll(SysOrgPackageMenusQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<sysOrgPackageMenusDto>
    */
    List<SysOrgPackageMenusDto> queryAll(SysOrgPackageMenusQueryCriteria criteria);
    /**
     * 查询租户套餐菜单关联表
     * 
     * @param id 租户套餐菜单关联表主键
     * @return 租户套餐菜单关联表
     */
    SysOrgPackageMenusDto selectSysOrgPackageMenusById(Integer id);


    /**
     * 新增租户套餐菜单关联表
     * 
     * @param sysOrgPackageMenus 租户套餐菜单关联表
     */
    void insertSysOrgPackageMenus(SysOrgPackageMenus sysOrgPackageMenus);

    /**
     * 修改租户套餐菜单关联表
     * 
     * @param sysOrgPackageMenus 租户套餐菜单关联表
     */
    void updateSysOrgPackageMenus(SysOrgPackageMenus sysOrgPackageMenus);

    /**
     * 批量删除租户套餐菜单关联表
     * 
     * @param ids 需要删除的租户套餐菜单关联表主键集合
     */
    void deleteSysOrgPackageMenusByIds(Integer[] ids);

    /**
     * 删除租户套餐菜单关联表信息
     * 
     * @param id 租户套餐菜单关联表主键
     */
    void deleteSysOrgPackageMenusById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<SysOrgPackageMenusDto> all, HttpServletResponse response) throws IOException;
}
