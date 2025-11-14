package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateReview;
import cc.efit.dialogue.biz.service.dto.TemplateReviewDto;
import cc.efit.dialogue.biz.service.dto.TemplateReviewQueryCriteria;
import cc.efit.dialogue.biz.vo.review.TemplateReviewInfo;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 话术审核记录Service接口
 * 
 * @author across
 * @date 2025-08-26
 */
public interface TemplateReviewService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateReviewDto> queryAll(TemplateReviewQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateReviewDto>
    */
    List<TemplateReviewDto> queryAll(TemplateReviewQueryCriteria criteria);
    /**
     * 查询话术审核记录
     * 
     * @param id 话术审核记录主键
     * @return 话术审核记录
     */
    TemplateReviewDto selectTemplateReviewById(Integer id);


    /**
     * 新增话术审核记录
     * 
     * @param templateReview 话术审核记录
     */
    void insertTemplateReview(TemplateReview templateReview);

    /**
     * 修改话术审核记录
     * 
     * @param templateReview 话术审核记录
     */
    void updateTemplateReview(TemplateReview templateReview);

    /**
     * 批量删除话术审核记录
     * 
     * @param ids 需要删除的话术审核记录主键集合
     */
    void deleteTemplateReviewByIds(Integer[] ids);

    /**
     * 删除话术审核记录信息
     * 
     * @param id 话术审核记录主键
     */
    void deleteTemplateReviewById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateReviewDto> all, HttpServletResponse response) throws IOException;

    void reviewTemplate(TemplateReviewInfo reviewInfo);
}
