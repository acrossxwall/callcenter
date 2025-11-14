package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateWordsCategory;
import cc.efit.dialogue.biz.service.dto.TemplateWordsCategoryDto;
import cc.efit.dialogue.biz.service.dto.TemplateWordsCategoryQueryCriteria;
import cc.efit.dialogue.biz.vo.words.CategoryInfo;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 词库分类Service接口
 * 
 * @author across
 * @date 2025-08-19
 */
public interface TemplateWordsCategoryService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateWordsCategoryDto> queryAll(TemplateWordsCategoryQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateWordsCategoryDto>
    */
    List<TemplateWordsCategoryDto> queryAll(TemplateWordsCategoryQueryCriteria criteria);
    /**
     * 查询词库分类
     * 
     * @param id 词库分类主键
     * @return 词库分类
     */
    TemplateWordsCategoryDto selectTemplateWordsCategoryById(Integer id);


    /**
     * 新增词库分类
     * 
     * @param templateWordsCategory 词库分类
     */
    void insertTemplateWordsCategory(TemplateWordsCategory templateWordsCategory);

    /**
     * 修改词库分类
     * 
     * @param templateWordsCategory 词库分类
     */
    void updateTemplateWordsCategory(TemplateWordsCategory templateWordsCategory);

    /**
     * 批量删除词库分类
     * 
     * @param ids 需要删除的词库分类主键集合
     */
    void deleteTemplateWordsCategoryByIds(Integer[] ids);

    /**
     * 删除词库分类信息
     * 
     * @param id 词库分类主键
     */
    void deleteTemplateWordsCategoryById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateWordsCategoryDto> all, HttpServletResponse response) throws IOException;

    List<CategoryInfo> getAllCategoryInfo();
}
