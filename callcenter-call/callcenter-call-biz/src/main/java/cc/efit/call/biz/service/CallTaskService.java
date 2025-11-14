package cc.efit.call.biz.service;

import java.util.List;

import cc.efit.call.api.domain.CallTask;
import cc.efit.call.api.vo.task.TaskSummaryInfo;
import cc.efit.call.biz.service.dto.CallTaskDto;
import cc.efit.call.biz.service.dto.CallTaskQueryCriteria;
import cc.efit.call.api.vo.task.CallTaskInfo;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 呼叫任务表Service接口
 * 
 * @author across
 * @date 2025-08-27
 */
public interface CallTaskService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallTaskDto> queryAll(CallTaskQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callTaskDto>
    */
    List<CallTaskDto> queryAll(CallTaskQueryCriteria criteria);
    /**
     * 查询呼叫任务表
     * 
     * @param id 呼叫任务表主键
     * @return 呼叫任务表
     */
    CallTaskDto selectCallTaskById(Integer id);


    /**
     * 新增呼叫任务表
     * 
     * @param callTask 呼叫任务表
     */
    void insertCallTask(CallTask callTask,Integer deptId);

    /**
     * 修改呼叫任务表
     * 
     * @param callTask 呼叫任务表
     */
    void updateCallTask(CallTask callTask,Integer deptId);

    /**
     * 批量删除呼叫任务表
     * 
     * @param ids 需要删除的呼叫任务表主键集合
     */
    void deleteCallTaskByIds(Integer[] ids);

    /**
     * 删除呼叫任务表信息
     * 
     * @param id 呼叫任务表主键
     */
    void deleteCallTaskById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallTaskDto> all, HttpServletResponse response) throws IOException;

    void switchTaskStatus(CallTask callTask);

    List<CallTaskInfo> findAllProcessingTask(Integer status);

    Long getTaskCountInfo();

    Map<Integer,Long> getTaskCallStatusCountInfo();
}
