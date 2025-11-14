package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateGlobalInteraction;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalInteractionDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalInteractionQueryCriteria;
import org.springframework.data.domain.Pageable;
import cc.efit.utils.PageResult;
/**
 * 交互全局设置Service接口
 * 
 * @author across
 * @date 2025-08-21
 */
public interface TemplateGlobalInteractionService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateGlobalInteractionDto> queryAll(TemplateGlobalInteractionQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateGlobalInteractionDto>
    */
    List<TemplateGlobalInteractionDto> queryAll(TemplateGlobalInteractionQueryCriteria criteria);
    /**
     * 查询交互全局设置
     * 
     * @param id 交互全局设置主键
     * @return 交互全局设置
     */
    TemplateGlobalInteractionDto selectTemplateGlobalInteractionById(Integer id);


    /**
     * 新增交互全局设置
     * 
     * @param templateGlobalInteraction 交互全局设置
     */
    void insertTemplateGlobalInteraction(TemplateGlobalInteraction templateGlobalInteraction);

    /**
     * 修改交互全局设置
     * 
     * @param templateGlobalInteraction 交互全局设置
     */
    void updateTemplateGlobalInteraction(TemplateGlobalInteraction templateGlobalInteraction);

    /**
     * 批量删除交互全局设置
     * 
     * @param ids 需要删除的交互全局设置主键集合
     */
    void deleteTemplateGlobalInteractionByIds(Integer[] ids);

    /**
     * 删除交互全局设置信息
     * 
     * @param id 交互全局设置主键
     */
    void deleteTemplateGlobalInteractionById(Integer id);

    TemplateGlobalInteraction findTemplateGlobalInteractionByCallTemplateId(Integer callTemplateId);
}
