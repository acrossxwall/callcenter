package cc.efit.dialogue.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateFlowBranchMapper;
import cc.efit.dialogue.biz.repository.TemplateFlowBranchRepository;
import cc.efit.dialogue.biz.service.dto.TemplateFlowBranchDto;
import cc.efit.dialogue.biz.service.dto.TemplateFlowBranchQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateFlowBranch;
import cc.efit.dialogue.biz.service.TemplateFlowBranchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 节点意图分支Service业务层处理
 * 
 * @author across
 * @date 2025-08-15
 */
@Service
@RequiredArgsConstructor
public class TemplateFlowBranchServiceImpl implements TemplateFlowBranchService  {

    private final TemplateFlowBranchRepository templateFlowBranchRepository;
    private final TemplateFlowBranchMapper templateFlowBranchMapper;

    @Override
    public PageResult<TemplateFlowBranchDto> queryAll(TemplateFlowBranchQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateFlowBranch> page = templateFlowBranchRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateFlowBranchMapper::toDto));
    }

    @Override
    public List<TemplateFlowBranchDto> queryAll(TemplateFlowBranchQueryCriteria criteria){
        return templateFlowBranchMapper.toDto(templateFlowBranchRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询节点意图分支
     * 
     * @param id 节点意图分支主键
     * @return 节点意图分支
     */
    @Override
    public TemplateFlowBranchDto selectTemplateFlowBranchById(Integer id)  {
        return templateFlowBranchMapper.toDto(templateFlowBranchRepository.findById(id).orElseGet(TemplateFlowBranch::new));
    }


    /**
     * 新增节点意图分支
     * 
     * @param templateFlowBranch 节点意图分支
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateFlowBranch(TemplateFlowBranch templateFlowBranch) {
        templateFlowBranchRepository.save(templateFlowBranch);
    }

    /**
     * 修改节点意图分支
     * 
     * @param templateFlowBranch 节点意图分支
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateFlowBranch(TemplateFlowBranch templateFlowBranch) {
        templateFlowBranchRepository.save(templateFlowBranch);
    }

    /**
     * 批量删除节点意图分支
     * 
     * @param ids 需要删除的节点意图分支主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateFlowBranchByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateFlowBranchById(id);
        }
    }

    /**
     * 删除节点意图分支信息
     * 
     * @param id 节点意图分支主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateFlowBranchById(Integer id) {
        templateFlowBranchRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateFlowBranchDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateFlowBranchDto templateFlowBranch : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateFlowBranch.getId());
            map.put("deptId",  templateFlowBranch.getDeptId());
            map.put("userId",  templateFlowBranch.getUserId());
            map.put("callTemplateId",  templateFlowBranch.getCallTemplateId());
            map.put("flowId",  templateFlowBranch.getFlowId());
            map.put("intentionId",  templateFlowBranch.getIntentionId());
            map.put("targetFlowId",  templateFlowBranch.getTargetFlowId());
            map.put("orgId",  templateFlowBranch.getOrgId());
            map.put("createBy",  templateFlowBranch.getCreateBy());
            map.put("updateBy",  templateFlowBranch.getUpdateBy());
            map.put("createTime",  templateFlowBranch.getCreateTime());
            map.put("deleted",  templateFlowBranch.getDeleted());
            map.put("updateTime",  templateFlowBranch.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
