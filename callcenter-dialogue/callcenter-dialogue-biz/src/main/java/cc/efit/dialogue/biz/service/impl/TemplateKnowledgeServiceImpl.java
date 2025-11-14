package cc.efit.dialogue.biz.service.impl;

import cc.efit.config.properties.FileProperties;
import cc.efit.dialogue.api.enums.TemplateVerbalEnum;
import cc.efit.dialogue.biz.config.TemplateConfig;
import cc.efit.dialogue.biz.repository.TemplateVerbalRepository;
import cc.efit.dialogue.biz.service.CallTemplateOperatorService;
import cc.efit.dialogue.biz.vo.knowledge.TemplateKnowledgeInfo;
import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.enums.CommonOperatorEnum;
import cc.efit.exception.BadRequestException;
import cc.efit.repository.LocalStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateKnowledgeMapper;
import cc.efit.dialogue.biz.repository.TemplateKnowledgeRepository;
import cc.efit.dialogue.biz.service.dto.TemplateKnowledgeDto;
import cc.efit.dialogue.biz.service.dto.TemplateKnowledgeQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateKnowledge;
import cc.efit.dialogue.biz.service.TemplateKnowledgeService;
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
 * 知识库Service业务层处理
 * 
 * @author across
 * @date 2025-08-16
 */
@Service
@RequiredArgsConstructor
public class TemplateKnowledgeServiceImpl extends AbstractTemplateCommonServiceImpl implements TemplateKnowledgeService  {

    private final TemplateKnowledgeRepository templateKnowledgeRepository;
    private final TemplateKnowledgeMapper templateKnowledgeMapper;
    private final TemplateVerbalRepository templateVerbalRepository;
    private final FileProperties fileProperties;
    private final TemplateConfig templateConfig;
    private final CallTemplateOperatorService callTemplateOperatorService;
    private final LocalStorageRepository localStorageRepository;
    @Override
    public PageResult<TemplateKnowledgeDto> queryAll(TemplateKnowledgeQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateKnowledge> page = templateKnowledgeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateKnowledgeMapper::toDto));
    }

    @Override
    public List<TemplateKnowledgeDto> queryAll(TemplateKnowledgeQueryCriteria criteria){
        return templateKnowledgeMapper.toDto(templateKnowledgeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询知识库
     * 
     * @param id 知识库主键
     * @return 知识库
     */
    @Override
    public TemplateKnowledgeVo selectTemplateKnowledgeById(Integer id)  {
        TemplateKnowledge knowledge = templateKnowledgeRepository.findById(id).orElseThrow(()-> new BadRequestException("知识库不存在"));
        return getTemplateKnowledgeVo(knowledge);
    }


    /**
     * 新增知识库
     * 
     * @param templateKnowledgeVo 知识库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateKnowledge(TemplateKnowledgeVo templateKnowledgeVo) {
        validTemplateKnowledgeName(templateKnowledgeVo);
        TemplateKnowledge templateKnowledge = new TemplateKnowledge();
        BeanUtils.copyProperties(templateKnowledgeVo, templateKnowledge);
        List<Integer> verbalIds = buildTemplateFlowVerbal(templateKnowledgeVo.getOrgId(), templateKnowledgeVo.getCallTemplateId(),
                null,templateKnowledgeVo.getName(),
                templateKnowledgeVo.getVerbalList(),
                TemplateVerbalEnum.Source.KNOWLEDGE,
                fileProperties.getPath().getBase(), templateConfig.getVerbalPath());
        templateKnowledge.setVerbalIds(verbalIds);
        templateKnowledgeRepository.save(templateKnowledge);
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateKnowledgeVo.getCallTemplateId(), CommonOperatorEnum.ADD.getCode(),  "新增知识库:" + templateKnowledgeVo.getName());
    }

    /**
     * 修改知识库
     * 
     * @param templateKnowledgeVo 知识库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateKnowledge(TemplateKnowledgeVo templateKnowledgeVo) {
        validTemplateKnowledgeName(templateKnowledgeVo);
        TemplateKnowledge templateKnowledge = templateKnowledgeRepository.findById(templateKnowledgeVo.getId()).orElse(null);
        if (templateKnowledge == null) {
            throw new BadRequestException("知识库不存在");
        }
        BeanUtils.copyProperties(templateKnowledgeVo, templateKnowledge);
        List<Integer> verbalIds = buildTemplateFlowVerbal(templateKnowledge.getOrgId(), templateKnowledge.getCallTemplateId(),
                templateKnowledge.getVerbalIds(),templateKnowledgeVo.getName(),
                templateKnowledgeVo.getVerbalList(),
                TemplateVerbalEnum.Source.KNOWLEDGE,
                fileProperties.getPath().getBase(), templateConfig.getVerbalPath());
        templateKnowledge.setVerbalIds(verbalIds);
        templateKnowledgeRepository.save(templateKnowledge);
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateKnowledgeVo.getCallTemplateId(), CommonOperatorEnum.UPDATE.getCode(),  "编辑知识库:" + templateKnowledgeVo.getName());
    }

    /**
     * 批量删除知识库
     * 
     * @param ids 需要删除的知识库主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateKnowledgeByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateKnowledgeById(id);
        }
    }

    /**
     * 删除知识库信息
     * 
     * @param id 知识库主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateKnowledgeById(Integer id) {
        TemplateKnowledge templateKnowledge = templateKnowledgeRepository.findById(id).orElse(null);
        if (templateKnowledge == null) {
            throw new BadRequestException("知识库不存在");
        }
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateKnowledge.getCallTemplateId(), CommonOperatorEnum.DELETE.getCode(),  "删除知识库:" + templateKnowledge.getName());
        templateKnowledgeRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateKnowledgeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateKnowledgeDto templateKnowledge : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateKnowledge.getId());
            map.put("name",  templateKnowledge.getName());
            map.put("type",  templateKnowledge.getType());
            map.put("intentionId",  templateKnowledge.getIntentionId());
            map.put("action",  templateKnowledge.getAction());
            map.put("targetFlowId",  templateKnowledge.getTargetFlowId());
            map.put("enableInterrupt",  templateKnowledge.getEnableInterrupt());
            map.put("smsTemplateId",  templateKnowledge.getSmsTemplateId());
            map.put("agentGroupId",  templateKnowledge.getAgentGroupId());
            map.put("callTemplateId",  templateKnowledge.getCallTemplateId());
            map.put("transferType",  templateKnowledge.getTransferType());
            map.put("transferNumber",  templateKnowledge.getTransferNumber());
            map.put("label",  templateKnowledge.getLabel());
            map.put("orgId",  templateKnowledge.getOrgId());
            map.put("userId",  templateKnowledge.getUserId());
            map.put("deptId",  templateKnowledge.getDeptId());
            map.put("createBy",  templateKnowledge.getCreateBy());
            map.put("updateBy",  templateKnowledge.getUpdateBy());
            map.put("createTime",  templateKnowledge.getCreateTime());
            map.put("deleted",  templateKnowledge.getDeleted());
            map.put("updateTime",  templateKnowledge.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TemplateKnowledgeInfo> selectTemplateKnowledgeInfoByCallTemplateId(Integer callTemplateId) {
        List<TemplateKnowledge> list = templateKnowledgeRepository.findByCallTemplateId(callTemplateId);
        return list==null?null:list.stream().map(t-> new TemplateKnowledgeInfo(t.getId(),t.getName())).toList();
    }

    @Override
    public TemplateVerbalRepository getVerbalRepository() {
        return templateVerbalRepository;
    }

    @Override
    public LocalStorageRepository getSystemLocalStorageRepository() {
        return localStorageRepository;
    }

    private void validTemplateKnowledgeName(TemplateKnowledgeVo templateKnowledgeVo) {
        String templateKnowledgeName = templateKnowledgeVo.getName();
        TemplateKnowledge templateKnowledge = templateKnowledgeRepository.findByNameAndCallTemplateId(templateKnowledgeName,templateKnowledgeVo.getCallTemplateId());
        if (templateKnowledge==null) {
            return ;
        }
        if (templateKnowledgeVo.getId()==null || !templateKnowledgeVo.getId().equals(templateKnowledge.getId())) {
            throw new BadRequestException("知识库名称重复");
        }
    }

}
