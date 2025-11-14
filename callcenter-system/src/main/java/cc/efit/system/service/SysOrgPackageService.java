package cc.efit.system.service;

import java.util.List;
import cc.efit.system.domain.SysOrgPackage;
import cc.efit.system.req.SysOrgPackageReq;
import cc.efit.system.res.OrgPackageInfo;
import cc.efit.system.res.SysOrgPackageRes;
import cc.efit.system.service.dto.SysOrgPackageDto;
import cc.efit.system.service.dto.SysOrgPackageQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.util.Set;

import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 租户套餐表Service接口
 * 
 * @author across
 * @date 2025-10-28
 */
public interface SysOrgPackageService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<SysOrgPackageDto> queryAll(SysOrgPackageQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<sysOrgPackageDto>
    */
    List<SysOrgPackageDto> queryAll(SysOrgPackageQueryCriteria criteria);
    /**
     * 查询租户套餐表
     * 
     * @param id 租户套餐表主键
     * @return 租户套餐表
     */
    SysOrgPackageRes selectSysOrgPackageById(Integer id);


    /**
     * 新增租户套餐表
     * 
     * @param req 租户套餐表
     */
    void insertSysOrgPackage(SysOrgPackageReq req);

    /**
     * 修改租户套餐表
     * 
     * @param req 租户套餐表
     */
    void updateSysOrgPackage(SysOrgPackageReq req);

    /**
     * 批量删除租户套餐表
     * 
     * @param ids 需要删除的租户套餐表主键集合
     */
    void deleteSysOrgPackageByIds(Integer[] ids);

    /**
     * 删除租户套餐表信息
     * 
     * @param id 租户套餐表主键
     */
    void deleteSysOrgPackageById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<SysOrgPackageDto> all, HttpServletResponse response) throws IOException;

    Set<Integer> buildPackageMenuIds(Integer packageId);

    List<OrgPackageInfo> getEnablePackageList();
}
