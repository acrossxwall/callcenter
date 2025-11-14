package cc.efit.dialogue.biz.service.impl;

import cc.efit.db.utils.QueryHelp;
import cc.efit.dialogue.api.enums.CallTemplateStatusEnum;
import cc.efit.dialogue.api.enums.TemplateFlowTypeEnum;
import cc.efit.dialogue.api.enums.TemplateIntentionClassifyEnum;
import cc.efit.dialogue.api.enums.TemplateIntentionTypeEnum;
import cc.efit.dialogue.api.vo.global.*;
import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.dialogue.api.vo.level.TemplateIntentionLevelInfo;
import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.dialogue.api.vo.template.TemplateInfo;
import cc.efit.dialogue.api.vo.template.TemplateInitInfo;
import cc.efit.dialogue.biz.domain.*;
import cc.efit.dialogue.biz.repository.*;
import cc.efit.dialogue.biz.service.CallTemplateOperatorService;
import cc.efit.dialogue.biz.vo.node.*;
import cc.efit.dialogue.biz.vo.template.TemplateFlowVo;
import cc.efit.enums.CommonOperatorEnum;
import cc.efit.enums.CommonStatusEnum;
import cc.efit.core.enums.YesNoEnum;
import cc.efit.exception.BadRequestException;
import cc.efit.exception.ServiceException;
import cc.efit.json.utils.JsonUtils;
import cc.efit.process.api.constants.DialogueRedisConstant;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.redis.utils.RedisUtils;
import cc.efit.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.CallTemplateMapper;
import cc.efit.dialogue.biz.service.dto.CallTemplateDto;
import cc.efit.dialogue.biz.service.dto.CallTemplateQueryCriteria;
import cc.efit.dialogue.biz.service.CallTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.stream.Collectors;

import static cc.efit.dialogue.api.constants.TemplateConstants.*;
import static cc.efit.process.api.constants.DialogueRedisConstant.*;

/**
 * ai拨打话术Service业务层处理
 * 
 * @author across
 * @date 2025-08-09
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CallTemplateServiceImpl extends AbstractTemplateCommonServiceImpl implements CallTemplateService  {

    private final CallTemplateRepository callTemplateRepository;
    private final CallTemplateMapper callTemplateMapper;
    private final TemplateFlowRepository templateFlowRepository;
    private final CallTemplateOperatorService callTemplateOperatorService;
    private final TemplateIntentionRepository templateIntentionRepository;
    private final TemplateFlowBranchRepository templateFlowBranchRepository;
    private final TemplateVerbalRepository templateVerbalRepository;
    private final TemplateIntentionLevelRepository templateIntentionLevelRepository;
    private final TemplateGlobalNoreplyRepository templateGlobalNoReplyRepository;
    private final TemplateGlobalInteractionRepository templateGlobalInteractionRepository;
    private final TemplateGlobalTtsRepository templateGlobalTtsRepository;
    private final TemplateGlobalNluRepository templateGlobalNluRepository;
    private final TemplateGlobalDefaultVerbalRepository templateGlobalDefaultVerbalRepository;
    private final TemplateReviewRepository templateReviewRepository;
    private final RedisUtils redisUtils;
//    private final ChatProcessVoipApi chatProcessVoipApi;
    private final TemplateKnowledgeRepository templateKnowledgeRepository;
    @Override
    public PageResult<CallTemplateDto> queryAll(CallTemplateQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<CallTemplate> page = callTemplateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(callTemplateMapper::toDto));
    }

    @Override
    public List<CallTemplateDto> queryAll(CallTemplateQueryCriteria criteria){
        return callTemplateMapper.toDto(callTemplateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询ai拨打话术
     * 
     * @param id ai拨打话术主键
     * @return ai拨打话术
     */
    @Override
    public CallTemplateDto selectCallTemplateById(Integer id)  {
        return callTemplateMapper.toDto(callTemplateRepository.findById(id).orElseGet(CallTemplate::new));
    }


    /**
     * 新增ai拨打话术
     * 
     * @param callTemplate ai拨打话术
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TemplateNodeVo insertCallTemplate(CallTemplate callTemplate) {
        callTemplate.setStatus(CommonStatusEnum.ENABLE.getCode());
        validCallTemplateName(callTemplate);
        callTemplateRepository.save(callTemplate);
        Integer id = callTemplate.getId();
        callTemplateOperatorService.buildCallTemplateOperatorLog(id, CommonOperatorEnum.ADD.getCode(), "新增ai拨打话术");
        buildTemplateGlobalInteraction(id);
        buildTemplateGlobalNoReply(id);
        buildTemplateGlobalTts(id);
        buildTemplateGlobalNlu(id);
        buildTemplateGlobalDefaultVerbal(id);
        return buildCallTemplateFlowAndIntention(id);
    }



    /**
     * 修改ai拨打话术
     * 
     * @param callTemplate ai拨打话术
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCallTemplate(CallTemplate callTemplate) {
        validCallTemplateName(callTemplate);
        CallTemplate template  = callTemplateRepository.findById(callTemplate.getId()).orElseGet(CallTemplate::new);
        if (template.getId()==null) {
            throw new BadRequestException("话术模板不存在");
        }
        if (CallTemplateStatusEnum.CHECK.getStatus().equals(template.getStatus())) {
            throw new BadRequestException("话术模板正在审核，不允许修改");
        }
        if (CallTemplateStatusEnum.PASS.getStatus().equals(template.getStatus())) {
            throw new BadRequestException("话术模板已审核通过，不允许修改");
        }
        template.setName(callTemplate.getName());
        template.setIndustry(callTemplate.getIndustry());
        template.setDescription(callTemplate.getDescription());
        callTemplateRepository.save(template);
        callTemplateOperatorService.buildCallTemplateOperatorLog(template.getId(), CommonOperatorEnum.UPDATE.getCode(), "修改ai拨打话术");
    }

    /**
     * 批量删除ai拨打话术
     * 
     * @param ids 需要删除的ai拨打话术主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTemplateByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteCallTemplateById(id);
        }
    }

    /**
     * 删除ai拨打话术信息
     * 
     * @param id ai拨打话术主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCallTemplateById(Integer id) {
        callTemplateOperatorService.buildCallTemplateOperatorLog(id, CommonOperatorEnum.DELETE.getCode(), "删除ai拨打话术");
        callTemplateRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<CallTemplateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CallTemplateDto callTemplate : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id",  callTemplate.getId());
            map.put("deptId",  callTemplate.getDeptId());
            map.put("userId",  callTemplate.getUserId());
            map.put("name",  callTemplate.getName());
            map.put("description",  callTemplate.getDescription());
            map.put("industry",  callTemplate.getIndustry());
            map.put("status",  callTemplate.getStatus());
            map.put("orgId",  callTemplate.getOrgId());
            map.put("createBy",  callTemplate.getCreateBy());
            map.put("updateBy",  callTemplate.getUpdateBy());
            map.put("createTime",  callTemplate.getCreateTime());
            map.put("deleted",  callTemplate.getDeleted());
            map.put("updateTime",  callTemplate.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public TemplateNodeVo selectCallTemplateFlowByCallTemplateId(Integer id) {
        List<TemplateFlow> flowList = templateFlowRepository.findByCallTemplateId(id);
        List<NodeInfo> list = flowList.stream().map(this::buildNodeInfoByTemplateFlow).toList();

        List<TemplateFlowBranch> templateFlowBranches = templateFlowBranchRepository.findByCallTemplateId(id);
        List<EdgeInfo> edges = templateFlowBranches.stream().map(TemplateFlowBranch::getEdgeInfo).filter(Objects::nonNull).toList();
        return new TemplateNodeVo(id, list,edges);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCallTemplateFlow(TemplateFlowVo flowVo) {
        //保存节点坐标
        flowVo.nodes().forEach(s->{
            String coordinate = s.x() + "," + s.y();
            templateFlowRepository.updateTemplateFlowCoordinate(s.id(),coordinate);
        });
        //保存节点连线
        flowVo.edges().forEach(s-> buildTemplateEdgeVo(s,flowVo.callTemplateId()));
    }

    @Override
    public void reviewCallTemplate(Integer id) {
        CallTemplate template  = callTemplateRepository.findById(id).orElseGet(CallTemplate::new);
        if (template.getId()==null) {
            throw new BadRequestException("话术模板不存在");
        }
        if (!CallTemplateStatusEnum.EDIT.getStatus().equals(template.getStatus())) {
            throw new BadRequestException("话术模板状态无法审核");
        }
        //TODO 校验节点连线是否合法
        //validTemplateFlowNodeAndEdge(flowVo);
        //校验通过
        TemplateReview review = new TemplateReview();
        review.setCallTemplateId(id);
        review.setStatus(CallTemplateStatusEnum.CHECK.getStatus());
        review.setName(template.getName());
        template.setStatus(CallTemplateStatusEnum.CHECK.getStatus());
        callTemplateRepository.save(template);
        templateReviewRepository.save(review);
    }

    @Override
    public List<TemplateInfo> selectReviewPassTemplate() {
        return callTemplateRepository.selectReviewPassTemplate();
    }

    @Override
    public TemplateInfo findTemplateInfoByCallTemplateId(Integer callTemplateId) {
        return callTemplateRepository.selectReviewPassTemplateById(callTemplateId);
    }

    @Override
    public TemplateInitInfo initTemplateInfoToRedis(Integer callTemplateId) {
        String templateInitKey = TEMPLATE_INIT_REDIS.formatted(callTemplateId);
        try {
            //简单判断只加载一次
            boolean notExists = redisUtils.setNx(templateInitKey, "1" );
            if (!notExists) {
                log.info("templateId:{} is init", callTemplateId);
                return null;
            }
            initTemplateFlowInfo(callTemplateId);

            initTemplateIntentionLevelInfo(callTemplateId);
            initTemplateGlobalSetting(callTemplateId);
//        initTemplateAsrInfo(callTemplateId);
//        initTemplateTtsInfo(callTemplateId);
            return initTemplateFlowBranchInfo(callTemplateId);
        } catch (Exception e) {
            log.error("template init error",  e);
            redisUtils.del(templateInitKey);
        }
        return null;
    }

    private void initTemplateIntentionLevelInfo(Integer callTemplateId) {
        List<TemplateIntentionLevel> list = templateIntentionLevelRepository.findByCallTemplateIdOrderBySortAsc(callTemplateId);
        if (list==null || list.isEmpty()) {
            return;
        }
        String levelKey = TEMPLATE_INTENTION_LEVEL_INFO.formatted(callTemplateId);
        List<TemplateIntentionLevelInfo> result = list.stream().map(s->new TemplateIntentionLevelInfo(s.getName(),s.getDescription(),s.getType(),s.getRuleContent())).toList();
        redisUtils.set(levelKey, JsonUtils.toJsonString(result));
    }

    private TemplateInitInfo initTemplateFlowBranchInfo(Integer callTemplateId) {
        //加载意图信息
        List<TemplateIntention> list = templateIntentionRepository.findByCallTemplateIdOrderBySortAsc(callTemplateId);
        //先存在redis一份，然后在本地缓存在存储一份
        String intentionKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_INTENTION_INFO,callTemplateId);
        Map<String,String > intentionMap = list.stream().filter(s-> StringUtils.isNotBlank(s.getKeywords())).collect(Collectors.toMap(s->s.getId().toString(),TemplateIntention::getKeywords));
        redisUtils.hsetAll(intentionKey,intentionMap);
        List<TemplateNodeBranchInfo> branchList = templateFlowBranchRepository.selectNodeBranchInfoByCallTemplateId(callTemplateId);
        Map<String,List<TemplateNodeBranchInfo>> branchMap = branchList.stream().collect(Collectors.groupingBy(s->s.flowId().toString()));
        String branchKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_FLOW_BRANCH_INFO,callTemplateId);
        redisUtils.hsetAll(branchKey,branchMap);
        Map<String, TemplateKnowledgeVo> knowledgeVoMap = initTemplateKnowledgeInfo(callTemplateId);
        return new TemplateInitInfo(callTemplateId,branchMap, intentionMap, knowledgeVoMap ) ;
    }

    private Map<String, TemplateKnowledgeVo> initTemplateKnowledgeInfo(Integer callTemplateId) {
        List<TemplateKnowledge> list = templateKnowledgeRepository.findByCallTemplateId(callTemplateId);
        if (list==null || list.isEmpty()) {
            return null;
        }
        Map<String, TemplateKnowledgeVo> knowledgeVoMap = list.stream().collect(Collectors.toMap(s->s.getId().toString(),this::getTemplateKnowledgeVo));
        String knowledgeKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_KNOWLEDGE_INFO,callTemplateId);
        redisUtils.hsetAll(knowledgeKey,knowledgeVoMap);
        return knowledgeVoMap;
    }

    private void initTemplateGlobalSetting(Integer callTemplateId) {
        TemplateGlobalNoreply info = templateGlobalNoReplyRepository.findByCallTemplateId(callTemplateId);
        if (info!=null && YesNoEnum.YES.getCode().equals(info.getEnableNoreply())) {
            TemplateGlobalNoReplyInfo noReplyInfo = buildTemplateGlobalNoRelayInfo(info);
            String noReplyKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_NO_REPLY, callTemplateId);
            redisUtils.set(noReplyKey, JsonUtils.toJsonString(noReplyInfo));
        }
        TemplateGlobalInteraction templateGlobalInteraction = templateGlobalInteractionRepository.findByCallTemplateId( callTemplateId);
        if (templateGlobalInteraction!=null && YesNoEnum.YES.getCode().equals(templateGlobalInteraction.getEnableInteraction())) {
            TemplateGlobalInteractionInfo interaction = buildTemplateGlobalInteractionInfo(templateGlobalInteraction);
            String interactionKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_INTERACTION, callTemplateId);
            redisUtils.set(interactionKey, JsonUtils.toJsonString(interaction));
        }
        if (templateGlobalInteraction!=null && YesNoEnum.YES.getCode().equals(templateGlobalInteraction.getEnableInterrupt())) {
            TemplateGlobalInterruptInfo interruptInfo = buildTemplateGlobalInterruptInfo(templateGlobalInteraction);
            String interruptKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_INTERRUPT, callTemplateId);
            redisUtils.set(interruptKey, JsonUtils.toJsonString(interruptInfo));
        }
        TemplateGlobalNlu nlu = templateGlobalNluRepository.findByCallTemplateId(callTemplateId);
        if (nlu!=null && YesNoEnum.YES.getCode().equals(nlu.getEnableNlu())) {
            TemplateGlobalNluInfo nluInfo = buildTemplateGlobalNluInfo(nlu);
            String nluInfoKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_NLU_INFO, callTemplateId);
            redisUtils.set(nluInfoKey, JsonUtils.toJsonString(nluInfo));
        }

        TemplateGlobalDefaultVerbal verbal = templateGlobalDefaultVerbalRepository.findByCallTemplateId(callTemplateId);
        if (verbal!=null && YesNoEnum.YES.getCode().equals(verbal.getEnableDefault())) {
            TemplateGlobalDefaultVerbalInfo verbalInfo = buildTemplateGlobalDefaultVerbalInfo(verbal);
            String verbalInfoKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_DEFAULT_INFO, callTemplateId);
            redisUtils.set(verbalInfoKey, JsonUtils.toJsonString(verbalInfo));
        }
    }

    private void initTemplateFlowInfo(Integer callTemplateId) {
        List<TemplateNodeInfo> templateNodeInfoList = buildTemplateNodeInfoList(callTemplateId);
        templateNodeInfoList.forEach(this::initTemplateNodeToRedis);
    }

    private void initTemplateNodeToRedis(TemplateNodeInfo templateNodeInfo) {
        Integer callTemplateId = templateNodeInfo.getCallTemplateId();
        String res = JsonUtils.toJsonString(templateNodeInfo);
        if (TemplateFlowTypeEnum.START.getCode().equals(templateNodeInfo.getNodeType())) {
            String startNodeKey = FormatUtil.formatTemplateRedisKey(DialogueRedisConstant.TEMPLATE_START_NODE_INFO, callTemplateId);
            redisUtils.set(startNodeKey, res);
        }
        String nodeKey = FormatUtil.formatTemplateRedisKey(DialogueRedisConstant.TEMPLATE_FLOW_NODE_INFO,templateNodeInfo.getCallTemplateId(),templateNodeInfo.getId());
        redisUtils.set(nodeKey, res);
    }

    private void buildTemplateEdgeVo(EdgeInfo edgeInfo, Integer callTemplateId) {
        Integer flowId = edgeInfo.sourceNodeId();
        Integer id = edgeInfo.sourceAnchorId();
        Integer targetFlowId = edgeInfo.targetNodeId();
        templateFlowBranchRepository.updateFlowBranchInfo(targetFlowId, edgeInfo, callTemplateId, flowId, id);
    }

    private void validTemplateFlowNodeAndEdge(TemplateFlowVo flowVo) {
        //校验连线规则， 1个意图只能链接一个节点 多个意图可以链接同一个节点
        // 节点不是跳转节点，且节点不是挂机节点和转人工节点，则下面的意图必须全部链接
        Set<Integer> set = new HashSet<>();
        flowVo.edges().forEach(s->validTemplateFlowEdge(s,set));
        //获取所有的连线起始的分支id
        Set<Integer> branchIds = flowVo.edges().stream().map(EdgeInfo::sourceAnchorId).collect(Collectors.toSet());
        //校验节点意图是否链接
        flowVo.nodes().forEach(s->validFlowIntentions(s, branchIds));
    }

    private void validTemplateFlowEdge(EdgeInfo edgeInfo,Set<Integer> set) {
        if (!set.add(edgeInfo.sourceAnchorId())) {
            //说明有一个意图链接两个节点
            throw new ServiceException("一个意图不能链接两个节点，请检查");
        }
    }

    private void validFlowIntentions(NodeInfo node, Set<Integer> branchIds) {
        Integer nodeId = node.id();
        TemplateFlow flow = templateFlowRepository.findById(nodeId).orElse(null);
        if (flow == null) {
            throw new ServiceException("节点不存在");
        }
        if (TemplateFlowTypeEnum.JUMP.getCode().equals(flow.getNodeType())
                || YesNoEnum.YES.getCode().equals(flow.getEnableHangup())
                || YesNoEnum.YES.getCode().equals(flow.getEnableTransfer())
            ) {
            //跳转节点 或 挂断节点 或转人工节点，则意图可以不链接
            return;
        }
        //校验意图是否链接
        for (NodeIntention intention : node.properties().intentions()) {
            if (!branchIds.contains(intention.id())) {
                //不包含说明此时普通节点的意图未链接
                throw new ServiceException("节点:" + flow.getNodeName()+ "的"+intention.name()+"意图未链接，请链接或设置为挂断节点");
            }
        }

    }

    private void validCallTemplateName(CallTemplate callTemplate) {
        String name = callTemplate.getName();
        CallTemplate template = callTemplateRepository.findByName(name);
        if (template==null) {
            return ;
        }
        if (callTemplate.getId()==null || !template.getId().equals(callTemplate.getId())) {
            throw new BadRequestException("话术模板名称重复");
        }
    }

    private TemplateNodeVo buildCallTemplateFlowAndIntention(Integer id) {
        TemplateFlow flow  = buildDefaultStartTemplateFlow(id);
        buildDefaultTemplateIntentionLevel(id);
        NodeIntention def = getNodeIntention(id, flow.getId(), INTENTION_BRANCH_DEFAULT, TemplateIntentionTypeEnum.DEFAULT,0, TemplateIntentionClassifyEnum.OTHER);
        NodeIntention no = getNodeIntention(id, flow.getId(), INTENTION_BRANCH_YES, TemplateIntentionTypeEnum.NORMAL,1, TemplateIntentionClassifyEnum.POSITIVE);
        NodeIntention yes = getNodeIntention(id, flow.getId(), INTENTION_BRANCH_NO, TemplateIntentionTypeEnum.NORMAL,2, TemplateIntentionClassifyEnum.NEGATIVE);
        NodeProperties properties = new NodeProperties(flow.getNodeName(),"", List.of(def,yes,no));
        NodeInfo info =  new NodeInfo(flow.getId(), "html-card",540,100, properties);
        return new TemplateNodeVo(id,List.of(info),null);
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

    @Override
    public TemplateIntentionLevelRepository getIntentionLevelRepository() {
        return templateIntentionLevelRepository;
    }

    @Override
    public TemplateGlobalInteractionRepository getGlobalInteractionRepository() {
        return templateGlobalInteractionRepository;
    }

    @Override
    public TemplateGlobalNoreplyRepository getGlobalNoReplyRepository() {
        return templateGlobalNoReplyRepository;
    }
    @Override
    public TemplateGlobalTtsRepository getGlobalTtsRepository() {
        return templateGlobalTtsRepository;
    }

    @Override
    public TemplateGlobalNluRepository getGlobalNluRepository() {
        return templateGlobalNluRepository;
    }

    @Override
    public TemplateGlobalDefaultVerbalRepository getGlobalDefaultVerbalRepository() {
        return templateGlobalDefaultVerbalRepository;
    }
}
