package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateVerbal;
import cc.efit.dialogue.biz.service.dto.TemplateVerbalDto;
import cc.efit.dialogue.biz.service.dto.TemplateVerbalQueryCriteria;
import cc.efit.dialogue.biz.vo.verbal.TemplateVerbalVo;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 话术Service接口
 * 
 * @author across
 * @date 2025-08-18
 */
public interface TemplateVerbalService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateVerbalDto> queryAll(TemplateVerbalQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateVerbalDto>
    */
    List<TemplateVerbalDto> queryAll(TemplateVerbalQueryCriteria criteria);
    /**
     * 查询话术
     * 
     * @param id 话术主键
     * @return 话术
     */
    TemplateVerbalDto selectTemplateVerbalById(Integer id);


    /**
     * 新增话术
     * 
     * @param templateVerbal 话术
     */
    void insertTemplateVerbal(TemplateVerbal templateVerbal);

    /**
     * 修改话术
     * 
     * @param templateVerbal 话术
     */
    void updateTemplateVerbal(TemplateVerbal templateVerbal);

    /**
     * 批量删除话术
     * 
     * @param ids 需要删除的话术主键集合
     */
    void deleteTemplateVerbalByIds(Integer[] ids);

    /**
     * 删除话术信息
     * 
     * @param id 话术主键
     */
    void deleteTemplateVerbalById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateVerbalDto> all, HttpServletResponse response) throws IOException;

    void handlerBatchImportTemplateVerbal(TemplateVerbalVo vo);
}
