package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateIntention;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionDto;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionQueryCriteria;
import cc.efit.dialogue.biz.vo.intention.IntentionInfo;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 意图分支Service接口
 * 
 * @author across
 * @date 2025-08-14
 */
public interface TemplateIntentionService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateIntentionDto> queryAll(TemplateIntentionQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateIntentionDto>
    */
    List<TemplateIntentionDto> queryAll(TemplateIntentionQueryCriteria criteria);
    /**
     * 查询意图分支
     * 
     * @param id 意图分支主键
     * @return 意图分支
     */
    TemplateIntentionDto selectTemplateIntentionById(Integer id);


    /**
     * 新增意图分支
     * 
     * @param templateIntention 意图分支
     */
    Integer insertTemplateIntention(TemplateIntention templateIntention);

    /**
     * 修改意图分支
     * 
     * @param templateIntention 意图分支
     */
    void updateTemplateIntention(TemplateIntention templateIntention);

    /**
     * 批量删除意图分支
     * 
     * @param ids 需要删除的意图分支主键集合
     */
    void deleteTemplateIntentionByIds(Integer[] ids);

    /**
     * 删除意图分支信息
     * 
     * @param id 意图分支主键
     */
    void deleteTemplateIntentionById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateIntentionDto> all, HttpServletResponse response) throws IOException;

    List<IntentionInfo> selectTemplateIntentionByCallTemplateId(Integer callTemplateId);
}
