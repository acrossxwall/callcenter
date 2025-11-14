package cc.efit.call.biz.service;

import java.util.List;
import cc.efit.call.api.domain.CallCustomer;
import cc.efit.call.api.enums.CallCustomerStatusEnum;
import cc.efit.call.biz.service.dto.CallCustomerDto;
import cc.efit.call.biz.service.dto.CallCustomerQueryCriteria;
import cc.efit.call.biz.vo.customer.ImportCustomerInfo;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 客户名单表Service接口
 * 
 * @author across
 * @date 2025-09-10
 */
public interface CallCustomerService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallCustomerDto> queryAll(CallCustomerQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callCustomerDto>
    */
    List<CallCustomerDto> queryAll(CallCustomerQueryCriteria criteria);
    /**
     * 查询客户名单表
     * 
     * @param id 客户名单表主键
     * @return 客户名单表
     */
    CallCustomerDto selectCallCustomerById(Integer id);


    /**
     * 新增客户名单表
     * 
     * @param callCustomer 客户名单表
     */
    void insertCallCustomer(CallCustomer callCustomer);

    /**
     * 修改客户名单表
     * 
     * @param callCustomer 客户名单表
     */
    void updateCallCustomer(CallCustomer callCustomer);

    /**
     * 批量删除客户名单表
     * 
     * @param ids 需要删除的客户名单表主键集合
     */
    void deleteCallCustomerByIds(Integer[] ids);

    /**
     * 删除客户名单表信息
     * 
     * @param id 客户名单表主键
     */
    void deleteCallCustomerById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallCustomerDto> all, HttpServletResponse response) throws IOException;

    /**
     * 导入号码
     * @param importReq
     */
    void importCustomer(ImportCustomerInfo importReq);

    /**
     * 开始调度拨打任务
     * @param taskId
     */
    void startDispatchCallTask(Integer taskId,Integer callTemplateId);

    long countCustomerByStatusAndTaskId(CallCustomerStatusEnum.CustomerStatus customerStatus, Integer taskId);
}
