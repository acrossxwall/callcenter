package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateIntentionLevel;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionLevelDto;
import cc.efit.dialogue.biz.service.dto.TemplateIntentionLevelQueryCriteria;
import cc.efit.dialogue.biz.vo.intention.IntentionLevelInfo;
import cc.efit.dialogue.biz.vo.intention.IntentionLevelSetSortInfo;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 意向等级Service接口
 * 
 * @author across
 * @date 2025-08-13
 */
public interface TemplateIntentionLevelService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateIntentionLevelDto> queryAll(TemplateIntentionLevelQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateIntentionLevelDto>
    */
    List<TemplateIntentionLevelDto> queryAll(TemplateIntentionLevelQueryCriteria criteria);
    /**
     * 查询意向等级
     * 
     * @param id 意向等级主键
     * @return 意向等级
     */
    TemplateIntentionLevelDto selectTemplateIntentionLevelById(Integer id);


    /**
     * 新增意向等级
     * 
     * @param templateIntentionLevel 意向等级
     */
    void insertTemplateIntentionLevel(TemplateIntentionLevel templateIntentionLevel);

    /**
     * 修改意向等级
     * 
     * @param templateIntentionLevel 意向等级
     */
    void updateTemplateIntentionLevel(TemplateIntentionLevel templateIntentionLevel);

    /**
     * 批量删除意向等级
     * 
     * @param ids 需要删除的意向等级主键集合
     */
    void deleteTemplateIntentionLevelByIds(Integer[] ids);

    /**
     * 删除意向等级信息
     * 
     * @param id 意向等级主键
     */
    void deleteTemplateIntentionLevelById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateIntentionLevelDto> all, HttpServletResponse response) throws IOException;

    List<IntentionLevelInfo> findTemplateIntentionLevelListByCallTemplateId(Integer callTemplateId);

    void setTemplateDefaultIntentionByCallTemplateId(Integer callTemplateId, String level);

    void setTemplateIntentionLevelSort(IntentionLevelSetSortInfo info);
}
