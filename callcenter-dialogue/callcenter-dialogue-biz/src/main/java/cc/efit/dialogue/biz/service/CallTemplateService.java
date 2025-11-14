package cc.efit.dialogue.biz.service;

import java.util.List;

import cc.efit.dialogue.api.vo.template.TemplateInfo;
import cc.efit.dialogue.api.vo.template.TemplateInitInfo;
import cc.efit.dialogue.biz.domain.CallTemplate;
import cc.efit.dialogue.biz.service.dto.CallTemplateDto;
import cc.efit.dialogue.biz.service.dto.CallTemplateQueryCriteria;
import cc.efit.dialogue.biz.vo.node.TemplateNodeVo;
import cc.efit.dialogue.biz.vo.template.TemplateFlowVo;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * ai拨打话术Service接口
 * 
 * @author across
 * @date 2025-08-09
 */
public interface CallTemplateService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<CallTemplateDto> queryAll(CallTemplateQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<callTemplateDto>
    */
    List<CallTemplateDto> queryAll(CallTemplateQueryCriteria criteria);
    /**
     * 查询ai拨打话术
     * 
     * @param id ai拨打话术主键
     * @return ai拨打话术
     */
    CallTemplateDto selectCallTemplateById(Integer id);


    /**
     * 新增ai拨打话术
     * 
     * @param callTemplate ai拨打话术
     */
    TemplateNodeVo insertCallTemplate(CallTemplate callTemplate);

    /**
     * 修改ai拨打话术
     * 
     * @param callTemplate ai拨打话术
     */
    void updateCallTemplate(CallTemplate callTemplate);

    /**
     * 批量删除ai拨打话术
     * 
     * @param ids 需要删除的ai拨打话术主键集合
     */
    void deleteCallTemplateByIds(Integer[] ids);

    /**
     * 删除ai拨打话术信息
     * 
     * @param id ai拨打话术主键
     */
    void deleteCallTemplateById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<CallTemplateDto> all, HttpServletResponse response) throws IOException;

    TemplateNodeVo selectCallTemplateFlowByCallTemplateId(Integer id);

    void saveCallTemplateFlow(TemplateFlowVo flowVo);

    void reviewCallTemplate(Integer id);

    List<TemplateInfo> selectReviewPassTemplate();

    TemplateInfo findTemplateInfoByCallTemplateId(Integer callTemplateId);

    TemplateInitInfo initTemplateInfoToRedis(Integer callTemplateId);
}
