package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateWords;
import cc.efit.dialogue.biz.service.dto.TemplateWordsDto;
import cc.efit.dialogue.biz.service.dto.TemplateWordsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 关键词库Service接口
 * 
 * @author across
 * @date 2025-08-19
 */
public interface TemplateWordsService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateWordsDto> queryAll(TemplateWordsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateWordsDto>
    */
    List<TemplateWordsDto> queryAll(TemplateWordsQueryCriteria criteria);
    /**
     * 查询关键词库
     * 
     * @param id 关键词库主键
     * @return 关键词库
     */
    TemplateWordsDto selectTemplateWordsById(Integer id);


    /**
     * 新增关键词库
     * 
     * @param templateWords 关键词库
     */
    void insertTemplateWords(TemplateWords templateWords);

    /**
     * 修改关键词库
     * 
     * @param templateWords 关键词库
     */
    void updateTemplateWords(TemplateWords templateWords);

    /**
     * 批量删除关键词库
     * 
     * @param ids 需要删除的关键词库主键集合
     */
    void deleteTemplateWordsByIds(Integer[] ids);

    /**
     * 删除关键词库信息
     * 
     * @param id 关键词库主键
     */
    void deleteTemplateWordsById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateWordsDto> all, HttpServletResponse response) throws IOException;

    List<String> selectAllByCategoryIds(List<Integer> categoryIds);
}
