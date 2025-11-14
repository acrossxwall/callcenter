package cc.efit.system.service;

import java.util.List;
import cc.efit.system.domain.SysOrg;
import cc.efit.system.service.dto.SysOrgDto;
import cc.efit.system.service.dto.SysOrgQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 多租户Service接口
 * 
 * @author across
 * @date 2025-08-06
 */
public interface SysOrgService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<SysOrgDto> queryAll(SysOrgQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<sysOrgDto>
    */
    List<SysOrgDto> queryAll(SysOrgQueryCriteria criteria);
    /**
     * 查询多租户
     * 
     * @param id 多租户主键
     * @return 多租户
     */
    SysOrgDto selectSysOrgById(Integer id);


    /**
     * 新增多租户
     * 
     * @param sysOrg 多租户
     */
    void insertSysOrg(SysOrg sysOrg);

    /**
     * 修改多租户
     * 
     * @param sysOrg 多租户
     */
    void updateSysOrg(SysOrg sysOrg);

    /**
     * 批量删除多租户
     * 
     * @param ids 需要删除的多租户主键集合
     */
    void deleteSysOrgByIds(Integer[] ids);

    /**
     * 删除多租户信息
     * 
     * @param id 多租户主键
     */
    void deleteSysOrgById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<SysOrgDto> all, HttpServletResponse response) throws IOException;

    List<SysOrgDto> getEnableOrgList();

    void updateOrgStatus(SysOrg resources);
}
