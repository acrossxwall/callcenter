package cc.efit.dialogue.biz.service.impl;

import cc.efit.config.properties.FileProperties;
import cc.efit.dialogue.api.enums.TemplateFlowTypeEnum;
import cc.efit.dialogue.api.enums.TemplateIntentionTypeEnum;
import cc.efit.dialogue.api.enums.TemplateVerbalEnum;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.dialogue.biz.config.TemplateConfig;
import cc.efit.dialogue.biz.domain.TemplateFlowBranch;
import cc.efit.dialogue.biz.domain.TemplateIntention;
import cc.efit.dialogue.biz.repository.TemplateFlowBranchRepository;
import cc.efit.dialogue.biz.repository.TemplateIntentionRepository;
import cc.efit.dialogue.biz.repository.TemplateVerbalRepository;
import cc.efit.dialogue.biz.service.CallTemplateOperatorService;
import cc.efit.dialogue.biz.vo.node.*;
import cc.efit.enums.CommonOperatorEnum;
import cc.efit.exception.BadRequestException;
import cc.efit.repository.LocalStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateFlowMapper;
import cc.efit.dialogue.biz.repository.TemplateFlowRepository;
import cc.efit.dialogue.biz.service.dto.TemplateFlowDto;
import cc.efit.dialogue.biz.service.dto.TemplateFlowQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateFlow;
import cc.efit.dialogue.biz.service.TemplateFlowService;
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
 * 流程节点Service业务层处理
 * 
 * @author across
 * @date 2025-08-14
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateFlowServiceImpl extends AbstractTemplateCommonServiceImpl implements TemplateFlowService  {

    private final TemplateFlowRepository templateFlowRepository;
    private final TemplateFlowMapper templateFlowMapper;
    private final CallTemplateOperatorService callTemplateOperatorService;
    private final TemplateFlowBranchRepository templateFlowBranchRepository;
    private final TemplateIntentionRepository templateIntentionRepository;
    private final TemplateVerbalRepository templateVerbalRepository;
    private final LocalStorageRepository localStorageRepository;
    private final FileProperties fileProperties;
    private final TemplateConfig templateConfig;
    @Override
    public PageResult<TemplateFlowDto> queryAll(TemplateFlowQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateFlow> page = templateFlowRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateFlowMapper::toDto));
    }

    @Override
    public List<TemplateFlowDto> queryAll(TemplateFlowQueryCriteria criteria){
        return templateFlowMapper.toDto(templateFlowRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询流程节点
     * 
     * @param id 流程节点主键
     * @return 流程节点
     */
    @Override
    public TemplateNodeInfo selectTemplateFlowById(Integer id)  {
        TemplateFlow flow = templateFlowRepository.findById(id).orElseThrow(()->new BadRequestException("流程节点不存在"));
        return buildTemplateNodeInfo( flow);
    }

    /**
     * 新增流程节点
     * 
     * @param templateFlow 流程节点
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NodeInfo insertTemplateFlow(TemplateFlow templateFlow) {
        String name = TemplateFlowTypeEnum.getTypeByCode(templateFlow.getNodeType());
        templateFlowRepository.save(templateFlow);
        templateFlow.setNodeName(name + "-" + templateFlow.getId());
        templateFlowRepository.save(templateFlow);
        List<NodeIntention> intentions = buildAddTemplateFlowIntention(templateFlow.getCallTemplateId(), templateFlow.getId(),templateFlow.getNodeType());
        NodeProperties properties = new NodeProperties(templateFlow.getNodeName(),"", intentions);
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateFlow.getCallTemplateId(), CommonOperatorEnum.ADD.getCode(),  "创建流程节点" + templateFlow.getNodeName());
        return new NodeInfo(templateFlow.getId(), "html-card",null,null, properties);
    }

    /**
     * 修改流程节点
     * 
     * @param webTemplateFlow 流程节点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NodeInfo updateTemplateFlow(TemplateNodeInfo webTemplateFlow) {
        log.info("收到edit template flow请求:{}",webTemplateFlow);
        Integer id = webTemplateFlow.getId();
        TemplateFlow templateFlow = templateFlowRepository.findById(id).orElseGet(TemplateFlow::new);
        if (templateFlow.getId()==null) {
            throw new BadRequestException("流程节点不存在");
        }
        if (!templateFlow.getNodeName().equals(webTemplateFlow.getNodeName())) {
            //修改流程节点，判断节点名称是否重复
            int c = templateFlowRepository.countByCallTemplateIdAndNodeName(templateFlow.getCallTemplateId(), webTemplateFlow.getNodeName());
            if (c>0) {
                throw new BadRequestException("流程节点名称已存在");
            }
            templateFlow.setNodeName(webTemplateFlow.getNodeName());
        }
        BeanUtils.copyProperties(webTemplateFlow, templateFlow);
        List<Integer> verbalIds = buildTemplateFlowVerbal(templateFlow.getOrgId(), templateFlow.getCallTemplateId(),
                 templateFlow.getVerbalIds(),templateFlow.getNodeName(),
                webTemplateFlow.getVerbalList(),
                TemplateVerbalEnum.Source.NODE,
                fileProperties.getPath().getBase(), templateConfig.getVerbalPath());
        templateFlow.setVerbalIds(verbalIds);
        templateFlowRepository.save(templateFlow);
        if (!TemplateFlowTypeEnum.JUMP.getCode().equals(templateFlow.getNodeType()) && webTemplateFlow.getIntentionIds()!=null && !webTemplateFlow.getIntentionIds().isEmpty()) {
            //非跳转节点才构建意图
            buildTemplateFlowIntention(templateFlow.getCallTemplateId(),templateFlow.getId(), webTemplateFlow.getIntentionIds());
        }
        callTemplateOperatorService.buildCallTemplateOperatorLog(templateFlow.getCallTemplateId(), CommonOperatorEnum.UPDATE.getCode(),  "修改流程节点" + templateFlow.getNodeName());

        return buildNodeInfoByTemplateFlow(templateFlow);
    }

    /**
     * 批量删除流程节点
     * 
     * @param ids 需要删除的流程节点主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateFlowByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateFlowById(id);
        }
    }

    /**
     * 删除流程节点信息
     * 
     * @param id 流程节点主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateFlowById(Integer id) {
        TemplateFlow flow = templateFlowRepository.findById(id).orElse(null);
        if (flow!=null && TemplateFlowTypeEnum.START.getCode().equals(flow.getNodeType())) {
            throw new BadRequestException( "开场白不能删除");
        }
        if (flow != null) {
            templateFlowRepository.logicDeleteById(id);
            callTemplateOperatorService.buildCallTemplateOperatorLog(flow.getCallTemplateId(), CommonOperatorEnum.DELETE.getCode(),  "删除节点:" + flow.getNodeName());
        }

    }


    @Override
    public void download(List<TemplateFlowDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateFlowDto templateFlow : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  templateFlow.getId());
            map.put("nodeName",  templateFlow.getNodeName());
            map.put("callTemplateId",  templateFlow.getCallTemplateId());
            map.put("enableHangup",  templateFlow.getEnableHangup());
            map.put("hangupMode",  templateFlow.getHangupMode());
            map.put("enableTransfer",  templateFlow.getEnableTransfer());
            map.put("transferAgentMobile",  templateFlow.getTransferNumber());
            map.put("transferType",  templateFlow.getTransferType());
            map.put("enableInterrupt",  templateFlow.getEnableInterrupt());
            map.put("enableMatchKnowledge",  templateFlow.getEnableMatchKnowledge());
            map.put("matchKnowledgeIds",  templateFlow.getMatchKnowledgeIds());
            map.put("ignoreReplyType",  templateFlow.getIgnoreReplyType());
            map.put("smsTemplateId",  templateFlow.getSmsTemplateId());
            map.put("nodeLabel",  templateFlow.getNodeLabel());
            map.put("nodeType",  templateFlow.getNodeType());
            map.put("coordinate",  templateFlow.getCoordinate());
            map.put("createTime",  templateFlow.getCreateTime());
            map.put("updateTime",  templateFlow.getUpdateTime());
            map.put("orgId",  templateFlow.getOrgId());
            map.put("userId",  templateFlow.getUserId());
            map.put("transferAgentGroupId",  templateFlow.getTransferAgentGroupId());
            map.put("triggerMatch",  templateFlow.getTriggerMatch());
            map.put("matchId",  templateFlow.getMatchId());
            map.put("createBy",  templateFlow.getCreateBy());
            map.put("updateBy",  templateFlow.getUpdateBy());
            map.put("deptId",  templateFlow.getDeptId());
            map.put("deleted",  templateFlow.getDeleted());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TemplateAllNodeInfo> findTemplateFlowByCallTemplateId(Integer callTemplateId) {
        List<TemplateFlow> list = templateFlowRepository.findByCallTemplateIdAndNodeTypeNot(callTemplateId, TemplateFlowTypeEnum.JUMP.getCode());
        return list==null?null:list.stream().map(s->new TemplateAllNodeInfo(s.getId(),s.getNodeName())).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFlowNodesAndEdges(TemplateNodeVo templateNodeInfo) {
        if (CollectionUtils.isNotEmpty(templateNodeInfo.nodes())) {
            //删除节点
            templateNodeInfo.nodes().forEach(node -> deleteTemplateFlow(templateNodeInfo.callTemplateId() ,node.id()));
        }
        if (CollectionUtils.isNotEmpty(templateNodeInfo.edges())) {
            //删除连线
            templateNodeInfo.edges().forEach(s->deleteTemplateFlowBranch(templateNodeInfo.callTemplateId(), s));
        }
    }

    @Override
    public List<String> findTemplateFlowLabels(Integer callTemplateId) {
        List<TemplateFlow> list = templateFlowRepository.findByCallTemplateId(callTemplateId);
        return list==null?null:list.stream().map(TemplateFlow::getNodeLabel).toList();
    }

    @Override
    public LocalStorageRepository getSystemLocalStorageRepository() {
        return localStorageRepository;
    }

    private void buildTemplateFlowIntention(Integer callTemplateId, Integer flowId, List<Integer> intentionIds) {
        List<Integer> oldIds = templateFlowBranchRepository.selectIntentionIdFindByCallTemplateIdAndFlowId(callTemplateId, flowId);
        List<Integer> insert = new ArrayList<>();
        int sort = 0;
        if (CollectionUtils.isEmpty(oldIds)) {
            //数据库为空,意图新增全部插入
            insert.addAll(intentionIds);
        }else{
            insert.addAll(intentionIds.stream().filter(id -> !oldIds.contains(id)).toList());
            List<Integer> delete = oldIds.stream().filter(id -> !intentionIds.contains(id)).toList() ;
            if (CollectionUtils.isNotEmpty(delete)) {
                //修改要删除,意图取消勾选
                delete.forEach(intentionId -> templateFlowBranchRepository.logicDeleteByCallTemplateIdAndFlowIdAndIntentionId(callTemplateId, flowId, intentionId));
            }
            //数据库保留的
            sort = oldIds.size() - delete.size();
        }
        if (CollectionUtils.isNotEmpty(insert)) {
            //需要新增
            List<TemplateFlowBranch> branches = new ArrayList<>();
            for (Integer id : insert) {
                branches.add(TemplateFlowBranch.builder().callTemplateId(callTemplateId).flowId(flowId)
                        .intentionId(id).sort(++sort).build()) ;
            }
            templateFlowBranchRepository.saveAll(branches);
        }
    }



    private List<NodeIntention> buildAddTemplateFlowIntention(Integer callTemplateId, Integer flowId,Integer nodeType) {
        if (TemplateFlowTypeEnum.JUMP.getCode().equals(nodeType)) {
            return null;
        }
        //查询模板默认的分支意图的id
        NodeIntention def = getDefaultIntentionInfo(callTemplateId, flowId);
        NodeIntention yes = getNormalIntentionInfo(callTemplateId, flowId, "肯定",1);
        NodeIntention no = getNormalIntentionInfo(callTemplateId, flowId, "否定",2);
        return List.of(def,yes,no);
    }

    private NodeIntention getDefaultIntentionInfo(Integer callTemplateId, Integer flowId) {
        TemplateIntention templateIntention = templateIntentionRepository.findByCallTemplateIdAndType(callTemplateId, TemplateIntentionTypeEnum.DEFAULT.getCode());
        Integer intentionId = templateIntention.getId();
        Integer branchId = buildFlowIntentionBranch(callTemplateId, flowId, intentionId,0);
        return getNodeIntention(branchId, templateIntention.getName());
    }


    private NodeIntention getNormalIntentionInfo(Integer callTemplateId, Integer flowId,String intentionName,Integer sort) {
        TemplateIntention templateIntention = templateIntentionRepository.findByCallTemplateIdAndTypeAndName(callTemplateId, TemplateIntentionTypeEnum.NORMAL.getCode(),intentionName);
        Integer intentionId = templateIntention.getId();
        Integer branchId = buildFlowIntentionBranch(callTemplateId, flowId, intentionId,sort);
        return getNodeIntention(branchId, intentionName);
    }

    private void deleteTemplateFlowBranch(Integer callTemplateId, EdgeInfo edge) {
        Integer branchId = edge.sourceAnchorId();
        templateFlowBranchRepository.logicDeleteByCallTemplateIdAndId(callTemplateId, branchId);
    }

    private void deleteTemplateFlow(Integer callTemplateId, Integer id) {
        deleteTemplateFlowById(id);
        //删除当前节点分支出发的连线
        templateFlowBranchRepository.logicDeleteByCallTemplateIdAndFlowId(callTemplateId,id);
        //删除链接到当前节点的分支连线
        templateFlowBranchRepository.logicDeleteByCallTemplateIdAndTargetFlowId(callTemplateId,id);
    }


    @Override
    public TemplateFlowRepository getFlowRepository() {
        return templateFlowRepository;
    }

    @Override
    public TemplateIntentionRepository getIntentionRepository() {
        return templateIntentionRepository;
    }

    @Override
    public TemplateFlowBranchRepository getFlowBranchRepository() {
        return templateFlowBranchRepository;
    }

    @Override
    public TemplateVerbalRepository getVerbalRepository() {
        return templateVerbalRepository;
    }
}
