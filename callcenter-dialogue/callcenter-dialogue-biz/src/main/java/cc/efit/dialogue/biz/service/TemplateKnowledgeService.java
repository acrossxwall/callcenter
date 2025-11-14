package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.service.dto.TemplateKnowledgeDto;
import cc.efit.dialogue.biz.service.dto.TemplateKnowledgeQueryCriteria;
import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.dialogue.biz.vo.knowledge.TemplateKnowledgeInfo;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 知识库Service接口
 * 
 * @author across
 * @date 2025-08-16
 */
public interface TemplateKnowledgeService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateKnowledgeDto> queryAll(TemplateKnowledgeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateKnowledgeDto>
    */
    List<TemplateKnowledgeDto> queryAll(TemplateKnowledgeQueryCriteria criteria);
    /**
     * 查询知识库
     * 
     * @param id 知识库主键
     * @return 知识库
     */
    TemplateKnowledgeVo selectTemplateKnowledgeById(Integer id);


    /**
     * 新增知识库
     * 
     * @param templateKnowledge 知识库
     */
    void insertTemplateKnowledge(TemplateKnowledgeVo templateKnowledge);

    /**
     * 修改知识库
     * 
     * @param templateKnowledge 知识库
     */
    void updateTemplateKnowledge(TemplateKnowledgeVo templateKnowledge);

    /**
     * 批量删除知识库
     * 
     * @param ids 需要删除的知识库主键集合
     */
    void deleteTemplateKnowledgeByIds(Integer[] ids);

    /**
     * 删除知识库信息
     * 
     * @param id 知识库主键
     */
    void deleteTemplateKnowledgeById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateKnowledgeDto> all, HttpServletResponse response) throws IOException;

    List <TemplateKnowledgeInfo> selectTemplateKnowledgeInfoByCallTemplateId(Integer callTemplateId);
}
