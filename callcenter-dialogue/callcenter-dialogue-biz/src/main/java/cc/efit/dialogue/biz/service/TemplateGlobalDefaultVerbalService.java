package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateGlobalDefaultVerbal;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalDefaultVerbalDto;
import cc.efit.dialogue.biz.service.dto.TemplateGlobalDefaultVerbalQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 兜底话术全局设置Service接口
 * 
 * @author across
 * @date 2025-11-11
 */
public interface TemplateGlobalDefaultVerbalService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateGlobalDefaultVerbalDto> queryAll(TemplateGlobalDefaultVerbalQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateGlobalDefaultVerbalDto>
    */
    List<TemplateGlobalDefaultVerbalDto> queryAll(TemplateGlobalDefaultVerbalQueryCriteria criteria);
    /**
     * 查询兜底话术全局设置
     * 
     * @param id 兜底话术全局设置主键
     * @return 兜底话术全局设置
     */
    TemplateGlobalDefaultVerbalDto selectTemplateGlobalDefaultVerbalById(Integer id);


    /**
     * 新增兜底话术全局设置
     * 
     * @param templateGlobalDefaultVerbal 兜底话术全局设置
     */
    void insertTemplateGlobalDefaultVerbal(TemplateGlobalDefaultVerbal templateGlobalDefaultVerbal);

    /**
     * 修改兜底话术全局设置
     * 
     * @param templateGlobalDefaultVerbal 兜底话术全局设置
     */
    void updateTemplateGlobalDefaultVerbal(TemplateGlobalDefaultVerbal templateGlobalDefaultVerbal);

    /**
     * 批量删除兜底话术全局设置
     * 
     * @param ids 需要删除的兜底话术全局设置主键集合
     */
    void deleteTemplateGlobalDefaultVerbalByIds(Integer[] ids);

    /**
     * 删除兜底话术全局设置信息
     * 
     * @param id 兜底话术全局设置主键
     */
    void deleteTemplateGlobalDefaultVerbalById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateGlobalDefaultVerbalDto> all, HttpServletResponse response) throws IOException;

    TemplateGlobalDefaultVerbal findTemplateGlobalDefaultVerbalByCallTemplateId(Integer callTemplateId);
}
