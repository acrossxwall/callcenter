package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateGlobalNlu;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNluDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNluQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * nlu全局设置Service接口
 * 
 * @author across
 * @date 2025-11-10
 */
public interface TemplateGlobalNluService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateGlobalNluDto> queryAll(TemplateGlobalNluQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateGlobalNluDto>
    */
    List<TemplateGlobalNluDto> queryAll(TemplateGlobalNluQueryCriteria criteria);
    /**
     * 查询nlu全局设置
     * 
     * @param id nlu全局设置主键
     * @return nlu全局设置
     */
    TemplateGlobalNluDto selectTemplateGlobalNluById(Integer id);


    /**
     * 新增nlu全局设置
     * 
     * @param templateGlobalNlu nlu全局设置
     */
    void insertTemplateGlobalNlu(TemplateGlobalNlu templateGlobalNlu);

    /**
     * 修改nlu全局设置
     * 
     * @param templateGlobalNlu nlu全局设置
     */
    void updateTemplateGlobalNlu(TemplateGlobalNlu templateGlobalNlu);

    /**
     * 批量删除nlu全局设置
     * 
     * @param ids 需要删除的nlu全局设置主键集合
     */
    void deleteTemplateGlobalNluByIds(Integer[] ids);

    /**
     * 删除nlu全局设置信息
     * 
     * @param id nlu全局设置主键
     */
    void deleteTemplateGlobalNluById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateGlobalNluDto> all, HttpServletResponse response) throws IOException;

    TemplateGlobalNlu findTemplateGlobalNluByCallTemplateId(Integer callTemplateId);
}
