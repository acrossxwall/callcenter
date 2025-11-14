package cc.efit.call.biz.service;

import java.util.List;
import cc.efit.call.biz.domain.CallCustomerImportDetail;
import cc.efit.call.biz.service.dto.CallCustomerImportDetailDto;
import cc.efit.call.biz.service.dto.CallCustomerImportDetailQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 客户名单批次详细表Service接口
 * 
 * @author across
 * @date 2025-09-12
 */
public interface CallCustomerImportDetailService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallCustomerImportDetailDto> queryAll(CallCustomerImportDetailQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callCustomerImportDetailDto>
    */
    List<CallCustomerImportDetailDto> queryAll(CallCustomerImportDetailQueryCriteria criteria);
    /**
     * 查询客户名单批次详细表
     * 
     * @param id 客户名单批次详细表主键
     * @return 客户名单批次详细表
     */
    CallCustomerImportDetailDto selectCallCustomerImportDetailById(Integer id);


    /**
     * 新增客户名单批次详细表
     * 
     * @param callCustomerImportDetail 客户名单批次详细表
     */
    void insertCallCustomerImportDetail(CallCustomerImportDetail callCustomerImportDetail);

    /**
     * 修改客户名单批次详细表
     * 
     * @param callCustomerImportDetail 客户名单批次详细表
     */
    void updateCallCustomerImportDetail(CallCustomerImportDetail callCustomerImportDetail);

    /**
     * 批量删除客户名单批次详细表
     * 
     * @param ids 需要删除的客户名单批次详细表主键集合
     */
    void deleteCallCustomerImportDetailByIds(Integer[] ids);

    /**
     * 删除客户名单批次详细表信息
     * 
     * @param id 客户名单批次详细表主键
     */
    void deleteCallCustomerImportDetailById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallCustomerImportDetailDto> all, HttpServletResponse response) throws IOException;
}
