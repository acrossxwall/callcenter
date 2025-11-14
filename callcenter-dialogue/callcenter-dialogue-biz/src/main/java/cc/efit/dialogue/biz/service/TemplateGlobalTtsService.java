package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateGlobalTts;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalTtsDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalTtsQueryCriteria;
import org.springframework.data.domain.Pageable;
import cc.efit.utils.PageResult;
/**
 * tts全局设置Service接口
 * 
 * @author across
 * @date 2025-08-21
 */
public interface TemplateGlobalTtsService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateGlobalTtsDto> queryAll(TemplateGlobalTtsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateGlobalTtsDto>
    */
    List<TemplateGlobalTtsDto> queryAll(TemplateGlobalTtsQueryCriteria criteria);
    /**
     * 查询tts全局设置
     * 
     * @param id tts全局设置主键
     * @return tts全局设置
     */
    TemplateGlobalTtsDto selectTemplateGlobalTtsById(Integer id);


    /**
     * 新增tts全局设置
     * 
     * @param templateGlobalTts tts全局设置
     */
    void insertTemplateGlobalTts(TemplateGlobalTts templateGlobalTts);

    /**
     * 修改tts全局设置
     * 
     * @param templateGlobalTts tts全局设置
     */
    void updateTemplateGlobalTts(TemplateGlobalTts templateGlobalTts);

    /**
     * 批量删除tts全局设置
     * 
     * @param ids 需要删除的tts全局设置主键集合
     */
    void deleteTemplateGlobalTtsByIds(Integer[] ids);

    /**
     * 删除tts全局设置信息
     * 
     * @param id tts全局设置主键
     */
    void deleteTemplateGlobalTtsById(Integer id);

    TemplateGlobalTts findTemplateGlobalTtsByCallTemplateId(Integer callTemplateId);
}
