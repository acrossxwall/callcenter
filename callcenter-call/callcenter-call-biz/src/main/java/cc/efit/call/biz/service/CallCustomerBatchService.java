package cc.efit.call.biz.service;

import java.util.List;
import cc.efit.call.biz.domain.CallCustomerBatch;
import cc.efit.call.biz.service.dto.CallCustomerBatchDto;
import cc.efit.call.biz.service.dto.CallCustomerBatchQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 客户名单批次表Service接口
 * 
 * @author across
 * @date 2025-09-10
 */
public interface CallCustomerBatchService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallCustomerBatchDto> queryAll(CallCustomerBatchQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callCustomerBatchDto>
    */
    List<CallCustomerBatchDto> queryAll(CallCustomerBatchQueryCriteria criteria);
    /**
     * 查询客户名单批次表
     * 
     * @param id 客户名单批次表主键
     * @return 客户名单批次表
     */
    CallCustomerBatchDto selectCallCustomerBatchById(Integer id);


    /**
     * 新增客户名单批次表
     * 
     * @param callCustomerBatch 客户名单批次表
     */
    void insertCallCustomerBatch(CallCustomerBatch callCustomerBatch);

    /**
     * 修改客户名单批次表
     * 
     * @param callCustomerBatch 客户名单批次表
     */
    void updateCallCustomerBatch(CallCustomerBatch callCustomerBatch);

    /**
     * 批量删除客户名单批次表
     * 
     * @param ids 需要删除的客户名单批次表主键集合
     */
    void deleteCallCustomerBatchByIds(Integer[] ids);

    /**
     * 删除客户名单批次表信息
     * 
     * @param id 客户名单批次表主键
     */
    void deleteCallCustomerBatchById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallCustomerBatchDto> all, HttpServletResponse response) throws IOException;
}
