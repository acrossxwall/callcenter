package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.CallTemplateOperator;
import cc.efit.dialogue.biz.service.dto.CallTemplateOperatorDto;
import cc.efit.dialogue.biz.service.dto.CallTemplateOperatorQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 话术模板操作日志Service接口
 * 
 * @author across
 * @date 2025-08-12
 */
public interface CallTemplateOperatorService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallTemplateOperatorDto> queryAll(CallTemplateOperatorQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callTemplateOperatorDto>
    */
    List<CallTemplateOperatorDto> queryAll(CallTemplateOperatorQueryCriteria criteria);
    /**
     * 查询话术模板操作日志
     * 
     * @param id 话术模板操作日志主键
     * @return 话术模板操作日志
     */
    CallTemplateOperatorDto selectCallTemplateOperatorById(Integer id);


    /**
     * 新增话术模板操作日志
     * 
     * @param callTemplateOperator 话术模板操作日志
     */
    void insertCallTemplateOperator(CallTemplateOperator callTemplateOperator);

    /**
     * 修改话术模板操作日志
     * 
     * @param callTemplateOperator 话术模板操作日志
     */
    void updateCallTemplateOperator(CallTemplateOperator callTemplateOperator);

    /**
     * 批量删除话术模板操作日志
     * 
     * @param ids 需要删除的话术模板操作日志主键集合
     */
    void deleteCallTemplateOperatorByIds(Integer[] ids);

    /**
     * 删除话术模板操作日志信息
     * 
     * @param id 话术模板操作日志主键
     */
    void deleteCallTemplateOperatorById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallTemplateOperatorDto> all, HttpServletResponse response) throws IOException;

    void buildCallTemplateOperatorLog(Integer callTemplateId, Integer type, String content);
}
