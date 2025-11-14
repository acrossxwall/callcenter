package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateGlobalNoreply;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNoreplyDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalNoreplyQueryCriteria;
import org.springframework.data.domain.Pageable;
import cc.efit.utils.PageResult;
/**
 * 无应答全局设置Service接口
 * 
 * @author across
 * @date 2025-08-21
 */
public interface TemplateGlobalNoreplyService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateGlobalNoreplyDto> queryAll(TemplateGlobalNoreplyQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateGlobalNoreplyDto>
    */
    List<TemplateGlobalNoreplyDto> queryAll(TemplateGlobalNoreplyQueryCriteria criteria);
    /**
     * 查询无应答全局设置
     * 
     * @param id 无应答全局设置主键
     * @return 无应答全局设置
     */
    TemplateGlobalNoreplyDto selectTemplateGlobalNoreplyById(Integer id);


    /**
     * 新增无应答全局设置
     * 
     * @param templateGlobalNoreply 无应答全局设置
     */
    void insertTemplateGlobalNoreply(TemplateGlobalNoreply templateGlobalNoreply);

    /**
     * 修改无应答全局设置
     * 
     * @param templateGlobalNoreply 无应答全局设置
     */
    void updateTemplateGlobalNoreply(TemplateGlobalNoreply templateGlobalNoreply);

    /**
     * 批量删除无应答全局设置
     * 
     * @param ids 需要删除的无应答全局设置主键集合
     */
    void deleteTemplateGlobalNoreplyByIds(Integer[] ids);

    /**
     * 删除无应答全局设置信息
     * 
     * @param id 无应答全局设置主键
     */
    void deleteTemplateGlobalNoreplyById(Integer id);

    TemplateGlobalNoreply findTemplateGlobalNoreplyByCallTemplateId(Integer callTemplateId);
}
