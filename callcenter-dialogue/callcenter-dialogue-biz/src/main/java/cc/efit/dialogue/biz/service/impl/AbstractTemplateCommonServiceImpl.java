package cc.efit.dialogue.biz.service.impl;

import cc.efit.dialogue.api.constants.TemplateConstants;
import cc.efit.dialogue.api.enums.*;
import cc.efit.dialogue.api.vo.global.*;
import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.dialogue.biz.domain.*;
import cc.efit.dialogue.biz.repository.*;
import cc.efit.dialogue.biz.service.TemplateDefaultService;
import cc.efit.dialogue.biz.utils.FilePathUtils;
import cc.efit.dialogue.biz.vo.node.NodeInfo;
import cc.efit.dialogue.biz.vo.node.NodeIntention;
import cc.efit.dialogue.api.vo.verbal.TemplateVerbalContentVo;
import cc.efit.dialogue.biz.vo.node.NodeProperties;
import cc.efit.domain.LocalStorage;
import cc.efit.core.enums.YesNoEnum;
import cc.efit.repository.LocalStorageRepository;
import cc.efit.utils.FileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static cc.efit.dialogue.api.constants.TemplateConstants.*;

public abstract class AbstractTemplateCommonServiceImpl implements TemplateDefaultService {
    /**
     * 获取意图模板 如果用到则需要 override
     * @return
     */
    public TemplateFlowRepository getFlowRepository() {
        return null;
    }
    public TemplateIntentionRepository getIntentionRepository() {
        return null;
    }
    public TemplateFlowBranchRepository getFlowBranchRepository() {
        return null;
    }
    public TemplateVerbalRepository getVerbalRepository(){
        return null;
    }
    public TemplateIntentionLevelRepository getIntentionLevelRepository() { return null; }
    public TemplateGlobalInteractionRepository getGlobalInteractionRepository(){return null;}
    public TemplateGlobalNoreplyRepository getGlobalNoReplyRepository(){return null;}
    public TemplateGlobalTtsRepository getGlobalTtsRepository(){return null;}
    public TemplateGlobalNluRepository getGlobalNluRepository(){return null;}
    public TemplateGlobalDefaultVerbalRepository getGlobalDefaultVerbalRepository(){return null;}
    public LocalStorageRepository getSystemLocalStorageRepository(){return null;}
    @Override
    public TemplateFlow buildDefaultStartTemplateFlow(Integer callTemplateId) {
        TemplateFlow flow = new TemplateFlow();
        flow.setCallTemplateId(callTemplateId);
        flow.setNodeName("开场白");
        flow.setNodeType(TemplateFlowTypeEnum.START.getCode());
        getFlowRepository().save(flow);
        return flow;
    }

    @Override
    public NodeIntention getNodeIntention(Integer callTemplateId, Integer flowId, String intentionName, TemplateIntentionTypeEnum type,
                                          Integer sort,TemplateIntentionClassifyEnum classify) {
        Integer intentionId = buildTemplateIntention(callTemplateId, intentionName,type,sort,classify);
        Integer branchId = buildFlowIntentionBranch(callTemplateId,flowId,intentionId,sort);
        return getNodeIntention(branchId,intentionName);
    }

    @Override
    public NodeIntention getNodeIntention(Integer branchId,  String intentionName) {
        return new NodeIntention(branchId,intentionName);
    }

    @Override
    public Integer buildFlowIntentionBranch(Integer callTemplateId, Integer flowId, Integer intentionId,Integer sort) {
        TemplateFlowBranch branch = TemplateFlowBranch.builder()
                .callTemplateId(callTemplateId)
                .flowId(flowId)
                .intentionId(intentionId)
                .sort(sort)
                .build();
        getFlowBranchRepository().save(branch);
        return branch.getId();
    }

    @Override
    public Integer buildTemplateIntention(Integer callTemplateId,  String intentionName,
                                      TemplateIntentionTypeEnum type, Integer sort,
                                      TemplateIntentionClassifyEnum classifyEnum) {
        TemplateIntention intention = new TemplateIntention();
        intention.setCallTemplateId(callTemplateId);
        intention.setName(intentionName);
        intention.setSort(sort);
        intention.setType(type.getCode());
        intention.setClassify(classifyEnum.getCode());
        getIntentionRepository().save(intention);
        return intention.getId();
    }

    @Override
    public Integer buildTemplateVerbal(Integer callTemplateId, String verbalName, String verbalContent, Integer source) {
        TemplateVerbal verbal = new TemplateVerbal();
        verbal.setCallTemplateId(callTemplateId);
        verbal.setType(TemplateVerbalEnum.Type.FILE.getCode());
        verbal.setStatus(TemplateVerbalEnum.Status.WAIT_UPLOAD.getCode());
        verbal.setSource(source);
        verbal.setName(verbalName);
        verbal.setContent(verbalContent);
        getVerbalRepository().save(verbal);
        return verbal.getId();
    }

    @Override
    public Integer buildTemplateVerbal(Integer callTemplateId, String verbalName, String verbalContent, Integer source,TemplateVerbalEnum.Status uploadStatus,
                                       String verbalPath) {
        TemplateVerbal verbal = new TemplateVerbal();
        verbal.setCallTemplateId(callTemplateId);
        verbal.setType(TemplateVerbalEnum.Type.FILE.getCode());
        verbal.setStatus(uploadStatus.getCode());
        verbal.setRecording(verbalPath);
        verbal.setSource(source);
        verbal.setName(verbalName);
        verbal.setContent(verbalContent);
        getVerbalRepository().save(verbal);
        return verbal.getId();
    }



    @Override
    public String getTemplateVerbalByIds(Integer callTemplateId,List<Integer> ids) {
        List<TemplateVerbal> list = getTemplateVerbalListByIds(callTemplateId,ids);
        return list ==null || list.isEmpty() || list.getFirst().getContent()==null ?"":list.getFirst().getContent();
    }

    @Override
    public List<TemplateVerbal> getTemplateVerbalListByIds(Integer callTemplateId, List<Integer> ids) {
        if (callTemplateId==null || ids==null || ids.isEmpty()) {
            return null;
        }
        TemplateVerbalRepository verbalRepository = getVerbalRepository();
        return  verbalRepository.findByCallTemplateIdAndIdIn(callTemplateId, ids);
    }

    public TemplateKnowledgeVo getTemplateKnowledgeVo(TemplateKnowledge knowledge) {
        TemplateKnowledgeVo vo = new TemplateKnowledgeVo();
        BeanUtils.copyProperties(knowledge,vo);
        List<TemplateVerbal> list = getTemplateVerbalListByIds(knowledge.getCallTemplateId(),knowledge.getVerbalIds());
        if (list ==null || list.isEmpty()) {
            vo.setVerbalList(null);
        }else{
            vo.setVerbalList(list.stream().map(s->{
                String filePath = s.getRecording();
                return new TemplateVerbalContentVo(null,filePath,s.getContent(), FileUtil.getFileNameByPath(filePath),s.getId());
            }).toList());
        }
        return vo;
    }

    @Override
    public void buildDefaultTemplateIntentionLevel(Integer callTemplateId) {
        buildTemplateIntentionLevel(callTemplateId, TemplateConstants.DEFAULT_INTENTION_LEVEL,TemplateConstants.DEFAULT_INTENTION_LEVEL_DESC, TemplateIntentionLevelTypeEnum.DEFAULT,1);
    }

    @Override
    public void buildTemplateIntentionLevel(Integer callTemplateId, String name, String desc, TemplateIntentionLevelTypeEnum type, Integer sort) {
        TemplateIntentionLevel level = new TemplateIntentionLevel();
        level.setName(name);
        level.setDescription(desc);
        level.setType(type.getType());
        level.setSort(sort);
        level.setCallTemplateId(callTemplateId);
        getIntentionLevelRepository().save(level);
    }

    @Override
    public void buildTemplateGlobalInteraction(Integer callTemplateId) {
        TemplateGlobalInteraction interaction = new TemplateGlobalInteraction();
        interaction.setCallTemplateId(callTemplateId);
        interaction.setEnableInterrupt(YesNoEnum.NO.getCode());
        interaction.setSeconds(TemplateConstants.NOT_INTERRUPT_SECONDS);
        interaction.setEnableInteraction(YesNoEnum.NO.getCode());
        interaction.setMaxInteractiveCount(TemplateConstants.MAX_INTERACTIVE_COUNT);
        interaction.setMaxDuration(TemplateConstants.MAX_DURATION);
        interaction.setInteractionAction(TemplateConstants.INTERACTION_ACTION_HANGUP);
        Integer verbalId = buildTemplateVerbal(callTemplateId,"全局设置交互话术", TemplateConstants.INTERACTION_VERBAL_CONTENT, TemplateVerbalEnum.Source.GLOBAL.getCode());
        interaction.setVerbalId(verbalId);
        getGlobalInteractionRepository().save(interaction);
    }

    @Override
    public void buildTemplateGlobalNoReply(Integer callTemplateId) {
        TemplateGlobalNoreply noReply = new TemplateGlobalNoreply();
        noReply.setCallTemplateId(callTemplateId);
        noReply.setEnableNoreply(YesNoEnum.NO.getCode());
        noReply.setMaxNoreplySeconds(TemplateConstants.MAX_NO_REPLY_SECONDS);
        noReply.setMaxNoreplyCount(TemplateConstants.MAX_NO_REPLY_COUNT);
        noReply.setNoreplyAction(TemplateConstants.NO_REPLY_ACTION_HANGUP);
        Integer verbalId = buildTemplateVerbal(callTemplateId,"全局设置无应答话术",
                TemplateConstants.NO_VERBAL_CONTENT, TemplateVerbalEnum.Source.GLOBAL.getCode());
        noReply.setVerbalId(verbalId);
        getGlobalNoReplyRepository().save(noReply);
    }

    @Override
    public void buildTemplateGlobalTts(Integer callTemplateId) {
        TemplateGlobalTts tts = new TemplateGlobalTts();
        tts.setEnableTts(YesNoEnum.NO.getCode());
        tts.setCallTemplateId(callTemplateId);
        getGlobalTtsRepository().save(tts);
    }

    @Override
    public String handlerSaveLocalTempFileToTemplate(Integer orgId, Integer callTemplateId, Integer fileId,String basePath,String verbalPath) {
        LocalStorage localStorage = getSystemLocalStorageRepository().findById(fileId).orElse(null);
        if (localStorage==null) {
            return null;
        }
        String relativePath = FilePathUtils.getTemplateFileRelativePath(verbalPath,orgId,callTemplateId);
        String filePath = basePath + relativePath;
        Path path  = Paths.get(filePath, localStorage.getRealName());
        FileUtil.copy(new File(localStorage.getPath()), path.toFile(), true);
        localStorage.setUsed(1);
        getSystemLocalStorageRepository().save(localStorage);
        return relativePath + localStorage.getRealName();
    }
    @Override
    public List<Integer> buildTemplateFlowVerbal(Integer orgId, Integer callTemplateId,
                                                  List<Integer> verbalIds,String name,
                                                 List<TemplateVerbalContentVo> webContents,
                                                 TemplateVerbalEnum.Source source,
                                                 String basePath,String templatePath) {
        if (CollectionUtils.isEmpty(webContents)) {
            //没有配置话术，直接
            return null;
        }
        List<Integer> verbalContentIds = webContents.stream().map(TemplateVerbalContentVo::verbalId).toList();
        List<TemplateVerbalContentVo> insert;
        List<TemplateVerbalContentVo> update = null;
        List<Integer> delete = null;
        List<Integer> ret = new ArrayList<>();
        if (verbalIds==null || verbalIds.isEmpty()) {
            //说明之前没有配置话术，直接新增
            insert = webContents;
        }else{
            insert = new ArrayList<>();
            update = new ArrayList<>();
            //之前有配置话术，需要判断是否需要新增
            delete = verbalIds.stream().filter(s-> !verbalContentIds.contains(s)).toList();
            for (TemplateVerbalContentVo vo: webContents) {
                if (StringUtils.isBlank(vo.verbalContent())) {
                    continue;
                }
                if (vo.verbalId()==null || !verbalIds.contains(vo.verbalId())) {
                    //新增的
                    insert.add(vo);
                    continue;
                }
                //需要修改的
                update.add(vo);
            }
        }
        if (delete != null && !delete.isEmpty()) {
            delete.forEach(getVerbalRepository()::logicDeleteById);
        }
        for (TemplateVerbalContentVo vo: insert) {
            String verbalFilePath = null;
            TemplateVerbalEnum.Status uploadStatus = TemplateVerbalEnum.Status.WAIT_UPLOAD;
            Integer fileId =  vo.fileId();
            if (fileId!=null) {
                verbalFilePath = handlerSaveLocalTempFileToTemplate( orgId, callTemplateId, fileId,basePath, templatePath);
                uploadStatus = TemplateVerbalEnum.Status.UPLOAD_SUCCESS;
            }
            ret.add(buildTemplateVerbal(callTemplateId, name,vo.verbalContent(), source.getCode(),uploadStatus,verbalFilePath));
        }
        if (CollectionUtils.isNotEmpty(update)) {
            for (TemplateVerbalContentVo vo: update) {
                Integer fileId =  vo.fileId();
                if (fileId!=null) {
                    String verbalFilePath = handlerSaveLocalTempFileToTemplate( orgId, callTemplateId, fileId,basePath, templatePath);
                    getVerbalRepository().updateTemplateVerbalFilePathById(vo.verbalId(),vo.verbalContent(),verbalFilePath );
                }else{
                    getVerbalRepository().updateTemplateVerbalById(vo.verbalId(),vo.verbalContent());
                }
                ret.add(vo.verbalId());
            }
        }

        return ret;
    }

    public TemplateGlobalInteractionInfo buildTemplateGlobalInteractionInfo(TemplateGlobalInteraction templateGlobalInteraction) {
        if (templateGlobalInteraction==null) {
            return new TemplateGlobalInteractionInfo(false, MAX_INTERACTIVE_COUNT,MAX_DURATION,INTERACTION_ACTION_HANGUP,
                    null,null,null,null,null);
        }
        TemplateVerbal verbal = getVerbalRepository().findById(templateGlobalInteraction.getVerbalId()).orElseGet(TemplateVerbal::new);
        boolean enable = YesNoEnum.YES.getCode().equals(templateGlobalInteraction.getEnableInteraction());
        return new TemplateGlobalInteractionInfo(enable, templateGlobalInteraction.getMaxInteractiveCount(),
                templateGlobalInteraction.getMaxDuration(),templateGlobalInteraction.getInteractionAction(),
                templateGlobalInteraction.getTargetFlowId(),verbal.getRecording(),verbal.getContent(),null, FileUtil.getFileNameByPath(verbal.getRecording()));
    }

    public void buildTemplateGlobalNlu(Integer callTemplateId) {
        TemplateGlobalNlu nlu = new TemplateGlobalNlu();
        nlu.setEnableNlu(YesNoEnum.NO.getCode());
        nlu.setCallTemplateId(callTemplateId);
        getGlobalNluRepository().save(nlu);
    }

    public void buildTemplateGlobalDefaultVerbal(Integer callTemplateId) {
        TemplateGlobalDefaultVerbal defaultVerbal = new TemplateGlobalDefaultVerbal();
        defaultVerbal.setCallTemplateId(callTemplateId);
        defaultVerbal.setEnableDefault(YesNoEnum.NO.getCode());
        Integer verbalId = buildTemplateVerbal(callTemplateId,"全局设置兜底话术",
                TemplateConstants.DEFAULT_VERBAL_CONTENT, TemplateVerbalEnum.Source.GLOBAL.getCode());
        defaultVerbal.setVerbalId(verbalId);
        defaultVerbal.setDefaultAction(TemplateConstants.DEFAULT_VERBAL_ACTION_HANGUP);
        getGlobalDefaultVerbalRepository().save(defaultVerbal);
    }

    public TemplateGlobalInterruptInfo buildTemplateGlobalInterruptInfo(TemplateGlobalInteraction templateGlobalInteraction) {
        if (templateGlobalInteraction== null) {
            return new TemplateGlobalInterruptInfo(false, NOT_INTERRUPT_SECONDS);
        }
        boolean enable = YesNoEnum.YES.getCode().equals(templateGlobalInteraction.getEnableInterrupt());
        return new TemplateGlobalInterruptInfo(enable, templateGlobalInteraction.getSeconds());
    }

    public TemplateNodeInfo buildTemplateNodeInfo( TemplateFlow flow) {
        Integer id = flow.getId();
        TemplateNodeInfo nodeInfo = new TemplateNodeInfo();
        BeanUtils.copyProperties(flow,nodeInfo);
        nodeInfo.setIntentionIds(getFlowBranchRepository().selectIntentionIdFindByCallTemplateIdAndFlowId(flow.getCallTemplateId(), id));
        List<TemplateVerbal> list = getTemplateVerbalListByIds(flow.getCallTemplateId(), flow.getVerbalIds());
        if (list ==null || list.isEmpty()) {
            nodeInfo.setVerbalList(null);
        }else{
            nodeInfo.setVerbalList(list.stream().map(s->{
                String filePath = s.getRecording();
                return new TemplateVerbalContentVo(null,filePath,s.getContent(), FileUtil.getFileNameByPath(filePath),s.getId());
            }).toList());
        }
        return nodeInfo;
    }

    public List<TemplateNodeInfo> buildTemplateNodeInfoList(Integer callTemplateId) {
        List<TemplateFlow> flows = getFlowRepository().findByCallTemplateId(callTemplateId);
        return flows.stream().map(this::buildTemplateNodeInfo).toList();
    }

    public TemplateGlobalNoReplyInfo buildTemplateGlobalNoRelayInfo(TemplateGlobalNoreply globalNoreply) {
        if (globalNoreply == null) {
            return new TemplateGlobalNoReplyInfo(false, MAX_NO_REPLY_COUNT,MAX_NO_REPLY_SECONDS,NO_REPLY_ACTION_HANGUP,
                    null,null,null,null,null);
        }
        TemplateVerbal verbal = getVerbalRepository().findById(globalNoreply.getVerbalId()).orElseGet(TemplateVerbal::new);
        boolean enable = YesNoEnum.YES.getCode().equals(globalNoreply.getEnableNoreply());
        return new TemplateGlobalNoReplyInfo(enable, globalNoreply.getMaxNoreplyCount(),
                globalNoreply.getMaxNoreplySeconds(), globalNoreply.getNoreplyAction(), globalNoreply.getTargetFlowId(),
                verbal.getRecording(),verbal.getContent(),null,FileUtil.getFileNameByPath(verbal.getRecording()));
    }

    public TemplateGlobalNluInfo buildTemplateGlobalNluInfo(TemplateGlobalNlu nlu) {
        if (nlu==null) {
            return new TemplateGlobalNluInfo(false , "" ,  0.5 );
        }
        return new TemplateGlobalNluInfo(YesNoEnum.YES.getCode().equals(nlu.getEnableNlu()), nlu.getModeId(), nlu.getThreshold());
    }

    public TemplateGlobalDefaultVerbalInfo buildTemplateGlobalDefaultVerbalInfo(TemplateGlobalDefaultVerbal defaultVerbal) {
        if (defaultVerbal==null) {
            return new TemplateGlobalDefaultVerbalInfo(false , DEFAULT_VERBAL_ACTION_HANGUP ,  null,null,null,null,null );
        }
        TemplateVerbal verbal = getVerbalRepository().findById(defaultVerbal.getVerbalId()).orElseGet(TemplateVerbal::new);
        boolean enable = YesNoEnum.YES.getCode().equals(defaultVerbal.getEnableDefault());
        return new TemplateGlobalDefaultVerbalInfo(enable,  defaultVerbal.getDefaultAction() , defaultVerbal.getTargetFlowId(),
                verbal.getRecording(),verbal.getContent(),null,FileUtil.getFileNameByPath(verbal.getRecording()));
    }



    public NodeInfo buildNodeInfoByTemplateFlow(TemplateFlow flow) {
        Integer id = flow.getId();
        String content = getTemplateVerbalByIds(flow.getCallTemplateId(), flow.getVerbalIds());
        List<NodeIntention> intentions  = getIntentionRepository().selectNodeIntentionByCallTemplateIdAndFlowId(flow.getCallTemplateId(),id);
        NodeProperties properties = new NodeProperties(flow.getNodeName(),content, intentions);
        String coordinate = flow.getCoordinate();
        Integer x = null;
        Integer y = null;
        if (coordinate!=null) {
            String[] split = coordinate.split(",");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
        }
        return new NodeInfo(id,"html-card", x, y, properties);
    }
}
