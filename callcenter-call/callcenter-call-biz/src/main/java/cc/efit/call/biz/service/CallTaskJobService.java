package cc.efit.call.biz.service;

import java.util.List;
import cc.efit.call.biz.domain.CallTaskJob;
import cc.efit.call.biz.service.dto.CallTaskJobDto;
import cc.efit.call.biz.service.dto.CallTaskJobQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 外呼任务job表Service接口
 * 
 * @author across
 * @date 2025-10-20
 */
public interface CallTaskJobService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallTaskJobDto> queryAll(CallTaskJobQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callTaskJobDto>
    */
    List<CallTaskJobDto> queryAll(CallTaskJobQueryCriteria criteria);
    /**
     * 查询外呼任务job表
     * 
     * @param id 外呼任务job表主键
     * @return 外呼任务job表
     */
    CallTaskJobDto selectCallTaskJobById(Integer id);


    /**
     * 新增外呼任务job表
     * 
     * @param callTaskJob 外呼任务job表
     */
    void insertCallTaskJob(CallTaskJob callTaskJob);

    /**
     * 修改外呼任务job表
     * 
     * @param callTaskJob 外呼任务job表
     */
    void updateCallTaskJob(CallTaskJob callTaskJob);

    /**
     * 批量删除外呼任务job表
     * 
     * @param ids 需要删除的外呼任务job表主键集合
     */
    void deleteCallTaskJobByIds(Integer[] ids);

    /**
     * 删除外呼任务job表信息
     * 
     * @param id 外呼任务job表主键
     */
    void deleteCallTaskJobById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallTaskJobDto> all, HttpServletResponse response) throws IOException;
}
