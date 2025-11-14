package cc.efit.call.biz.service;

import java.util.List;
import cc.efit.call.api.domain.CallSystemStatistics;
import cc.efit.call.api.vo.task.IndexStatisticsInfo;
import cc.efit.call.biz.service.dto.CallSystemStatisticsDto;
import cc.efit.call.biz.service.dto.CallSystemStatisticsQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 呼叫任务系统维度统计表Service接口
 * 
 * @author across
 * @date 2025-10-22
 */
public interface CallSystemStatisticsService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallSystemStatisticsDto> queryAll(CallSystemStatisticsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callSystemStatisticsDto>
    */
    List<CallSystemStatisticsDto> queryAll(CallSystemStatisticsQueryCriteria criteria);
    /**
     * 查询呼叫任务系统维度统计表
     * 
     * @param id 呼叫任务系统维度统计表主键
     * @return 呼叫任务系统维度统计表
     */
    CallSystemStatisticsDto selectCallSystemStatisticsById(Integer id);


    /**
     * 新增呼叫任务系统维度统计表
     * 
     * @param callSystemStatistics 呼叫任务系统维度统计表
     */
    void insertCallSystemStatistics(CallSystemStatistics callSystemStatistics);

    /**
     * 修改呼叫任务系统维度统计表
     * 
     * @param callSystemStatistics 呼叫任务系统维度统计表
     */
    void updateCallSystemStatistics(CallSystemStatistics callSystemStatistics);

    /**
     * 批量删除呼叫任务系统维度统计表
     * 
     * @param ids 需要删除的呼叫任务系统维度统计表主键集合
     */
    void deleteCallSystemStatisticsByIds(Integer[] ids);

    /**
     * 删除呼叫任务系统维度统计表信息
     * 
     * @param id 呼叫任务系统维度统计表主键
     */
    void deleteCallSystemStatisticsById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallSystemStatisticsDto> all, HttpServletResponse response) throws IOException;

    IndexStatisticsInfo getSystemStatisticsInfo();
}
