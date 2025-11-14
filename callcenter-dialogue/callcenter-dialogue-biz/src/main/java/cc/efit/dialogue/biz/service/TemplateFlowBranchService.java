package cc.efit.dialogue.biz.service;

import java.util.List;
import cc.efit.dialogue.biz.domain.TemplateFlowBranch;
import cc.efit.dialogue.biz.service.dto.TemplateFlowBranchDto;
import cc.efit.dialogue.biz.service.dto.TemplateFlowBranchQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import cc.efit.utils.PageResult;
/**
 * 节点意图分支Service接口
 * 
 * @author across
 * @date 2025-08-15
 */
public interface TemplateFlowBranchService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<TemplateFlowBranchDto> queryAll(TemplateFlowBranchQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<templateFlowBranchDto>
    */
    List<TemplateFlowBranchDto> queryAll(TemplateFlowBranchQueryCriteria criteria);
    /**
     * 查询节点意图分支
     * 
     * @param id 节点意图分支主键
     * @return 节点意图分支
     */
    TemplateFlowBranchDto selectTemplateFlowBranchById(Integer id);


    /**
     * 新增节点意图分支
     * 
     * @param templateFlowBranch 节点意图分支
     */
    void insertTemplateFlowBranch(TemplateFlowBranch templateFlowBranch);

    /**
     * 修改节点意图分支
     * 
     * @param templateFlowBranch 节点意图分支
     */
    void updateTemplateFlowBranch(TemplateFlowBranch templateFlowBranch);

    /**
     * 批量删除节点意图分支
     * 
     * @param ids 需要删除的节点意图分支主键集合
     */
    void deleteTemplateFlowBranchByIds(Integer[] ids);

    /**
     * 删除节点意图分支信息
     * 
     * @param id 节点意图分支主键
     */
    void deleteTemplateFlowBranchById(Integer id);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TemplateFlowBranchDto> all, HttpServletResponse response) throws IOException;
}
