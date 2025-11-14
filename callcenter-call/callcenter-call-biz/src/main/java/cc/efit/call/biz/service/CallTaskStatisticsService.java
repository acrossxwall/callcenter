package cc.efit.call.biz.service;

import java.util.List;
import cc.efit.call.api.domain.CallTaskStatistics;
import cc.efit.call.biz.service.dto.CallTaskStatisticsDto;
import cc.efit.call.biz.service.dto.CallTaskStatisticsQueryCriteria;
import cc.efit.call.api.vo.task.TaskStatisticsInfo;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 呼叫任务统计表表Service接口
 * 
 * @author across
 * @date 2025-10-15
 */
public interface CallTaskStatisticsService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallTaskStatisticsDto> queryAll(CallTaskStatisticsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callTaskStatisticsDto>
    */
    List<CallTaskStatisticsDto> queryAll(CallTaskStatisticsQueryCriteria criteria);
    /**
     * 查询呼叫任务统计表表
     * 
     * @param id 呼叫任务统计表表主键
     * @return 呼叫任务统计表表
     */
    CallTaskStatisticsDto selectCallTaskStatisticsById(Integer id);


    /**
     * 新增呼叫任务统计表表
     * 
     * @param callTaskStatistics 呼叫任务统计表表
     */
    void insertCallTaskStatistics(CallTaskStatistics callTaskStatistics);

    /**
     * 修改呼叫任务统计表表
     * 
     * @param callTaskStatistics 呼叫任务统计表表
     */
    void updateCallTaskStatistics(CallTaskStatistics callTaskStatistics);

    /**
     * 批量删除呼叫任务统计表表
     * 
     * @param ids 需要删除的呼叫任务统计表表主键集合
     */
    void deleteCallTaskStatisticsByIds(Integer[] ids);

    /**
     * 删除呼叫任务统计表表信息
     * 
     * @param id 呼叫任务统计表表主键
     */
    void deleteCallTaskStatisticsById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallTaskStatisticsDto> all, HttpServletResponse response) throws IOException;

    TaskStatisticsInfo selectCallTaskStatisticsInfo();
}
