package cc.efit.call.biz.service;

import java.util.List;
import cc.efit.call.api.domain.CallRecord;
import cc.efit.call.biz.service.dto.CallRecordDto;
import cc.efit.call.biz.service.dto.CallRecordQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 呼叫记录表Service接口
 * 
 * @author across
 * @date 2025-09-26
 */
public interface CallRecordService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallRecordDto> queryAll(CallRecordQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callRecordDto>
    */
    List<CallRecordDto> queryAll(CallRecordQueryCriteria criteria);
    /**
     * 查询呼叫记录表
     * 
     * @param id 呼叫记录表主键
     * @return 呼叫记录表
     */
    CallRecordDto selectCallRecordById(Integer id);


    /**
     * 新增呼叫记录表
     * 
     * @param callRecord 呼叫记录表
     */
    void insertCallRecord(CallRecord callRecord);

    /**
     * 修改呼叫记录表
     * 
     * @param callRecord 呼叫记录表
     */
    void updateCallRecord(CallRecord callRecord);

    /**
     * 批量删除呼叫记录表
     * 
     * @param ids 需要删除的呼叫记录表主键集合
     */
    void deleteCallRecordByIds(Integer[] ids);

    /**
     * 删除呼叫记录表信息
     * 
     * @param id 呼叫记录表主键
     */
    void deleteCallRecordById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallRecordDto> all, HttpServletResponse response) throws IOException;
}
