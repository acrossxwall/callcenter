package cc.efit.dialogue.biz.service.impl;

import cc.efit.config.properties.FileProperties;
import cc.efit.dialogue.api.vo.global.*;
import cc.efit.dialogue.biz.config.TemplateConfig;
import cc.efit.dialogue.biz.domain.*;
import cc.efit.dialogue.biz.repository.TemplateVerbalRepository;
import cc.efit.dialogue.biz.service.*;
import cc.efit.dialogue.biz.vo.global.*;
import cc.efit.enums.CommonOperatorEnum;
import cc.efit.core.enums.YesNoEnum;
import cc.efit.repository.LocalStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateGlobalSettingsServiceImpl extends AbstractTemplateCommonServiceImpl implements TemplateGlobalSettingsService {
    private final TemplateGlobalInteractionService templateGlobalInteractionService;
    private final TemplateGlobalNoreplyService templateGlobalNoreplyService;
    private final TemplateGlobalTtsService templateGlobalTtsService;
    private final TemplateGlobalNluService templateGlobalNluService;
    private final TemplateGlobalDefaultVerbalService templateGlobalDefaultVerbalService;
    private final TemplateVerbalRepository verbalRepository;
    private final CallTemplateOperatorService templateOperatorService;
    private final LocalStorageRepository localStorageRepository;
    private final FileProperties fileProperties;
    private final TemplateConfig templateConfig;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTemplateGlobalSettings(TemplateGlobalSettingInfo info) {
        handlerSaveTemplateGlobalInterruptAndInteraction(info.callTemplateId(), info );
        handlerSaveTemplateGlobalNoreply(info.callTemplateId(),info.noReply());
        handlerSaveTemplateGlobalTts(info.callTemplateId(),info.tts());
        handlerSaveTemplateGlobalNlu(info.callTemplateId(), info.nlu());
        handlerSaveTemplateDefaultVerbal(info.callTemplateId(), info.defaultVerbal());
        templateOperatorService.buildCallTemplateOperatorLog(info.callTemplateId(), CommonOperatorEnum.UPDATE.getCode(), "修改话术全局设置");
    }

    @Override
    public TemplateGlobalSettingInfo getTemplateGlobalSettings(Integer id) {
        TemplateGlobalInteraction templateGlobalInteraction = templateGlobalInteractionService.findTemplateGlobalInteractionByCallTemplateId( id);
        TemplateGlobalInteractionInfo interaction = buildTemplateGlobalInteractionInfo(templateGlobalInteraction);
        TemplateGlobalInterruptInfo interrupt = buildTemplateGlobalInterruptInfo(templateGlobalInteraction);
        TemplateGlobalNoreply globalNoreply = templateGlobalNoreplyService.findTemplateGlobalNoreplyByCallTemplateId(id);
        TemplateGlobalTts globalTts = templateGlobalTtsService.findTemplateGlobalTtsByCallTemplateId(id);
        TemplateGlobalTtsInfo tts = buildTemplateGlobalTtsInfo(globalTts);
        TemplateGlobalNoReplyInfo noRelay = buildTemplateGlobalNoRelayInfo(globalNoreply);
        TemplateGlobalAdvanceInfo advanceInfo = new TemplateGlobalAdvanceInfo(false,false,3,false,false);
        TemplateGlobalNlu nlu = templateGlobalNluService.findTemplateGlobalNluByCallTemplateId(id);
        TemplateGlobalNluInfo nluInfo = buildTemplateGlobalNluInfo(nlu);
        TemplateGlobalDefaultVerbal defaultVerbalInfo = templateGlobalDefaultVerbalService.findTemplateGlobalDefaultVerbalByCallTemplateId(id);
        TemplateGlobalDefaultVerbalInfo verbalInfo = buildTemplateGlobalDefaultVerbalInfo(defaultVerbalInfo);
        return new TemplateGlobalSettingInfo(id,interrupt,interaction,noRelay, tts,advanceInfo,nluInfo,verbalInfo);
    }

    private void handlerSaveTemplateGlobalInterruptAndInteraction(Integer callTemplateId, TemplateGlobalSettingInfo info) {
        TemplateGlobalInterruptInfo interrupt = info.interrupt();
        TemplateGlobalInteractionInfo interaction = info.interaction();
        TemplateGlobalInteraction dbInteraction = templateGlobalInteractionService.findTemplateGlobalInteractionByCallTemplateId(callTemplateId);
        if (interrupt.enabled()) {
            //打断设置开启
            dbInteraction.setEnableInterrupt(YesNoEnum.YES.getCode());
            dbInteraction.setSeconds(interrupt.seconds());
        }else {
            dbInteraction.setEnableInterrupt(YesNoEnum.NO.getCode());
        }
        if (interaction.enabled()) {
            dbInteraction.setEnableInteraction(YesNoEnum.YES.getCode());
            dbInteraction.setInteractionAction(interaction.interactionAction());
            dbInteraction.setMaxInteractiveCount(interaction.maxInteractiveCount());
            dbInteraction.setMaxDuration(interaction.maxDuration());
            dbInteraction.setTargetFlowId(interaction.targetFlowId());
            buildTemplateGlobalVerbal(dbInteraction.getOrgId(), dbInteraction.getCallTemplateId(), interaction.fileId(),dbInteraction.getVerbalId(),interaction.verbalFileText());
        }else{
            dbInteraction.setEnableInteraction(YesNoEnum.NO.getCode());
        }
        templateGlobalInteractionService.updateTemplateGlobalInteraction(dbInteraction);
    }

    private void handlerSaveTemplateGlobalNoreply(Integer callTemplateId, TemplateGlobalNoReplyInfo templateGlobalNoRelayInfo) {
        TemplateGlobalNoreply dbNoReply = templateGlobalNoreplyService.findTemplateGlobalNoreplyByCallTemplateId(callTemplateId);
        dbNoReply.setEnableNoreply(templateGlobalNoRelayInfo.enabled()? YesNoEnum.YES.getCode(): YesNoEnum.NO.getCode());
        dbNoReply.setNoreplyAction(templateGlobalNoRelayInfo.noreplyAction());
        dbNoReply.setMaxNoreplyCount(templateGlobalNoRelayInfo.maxNoreplyCount());
        dbNoReply.setMaxNoreplySeconds(templateGlobalNoRelayInfo.maxNoreplySeconds());
        dbNoReply.setTargetFlowId(templateGlobalNoRelayInfo.targetFlowId());
        buildTemplateGlobalVerbal(dbNoReply.getOrgId(), dbNoReply.getCallTemplateId(), templateGlobalNoRelayInfo.fileId(),dbNoReply.getVerbalId(),templateGlobalNoRelayInfo.verbalFileText());
        templateGlobalNoreplyService.updateTemplateGlobalNoreply(dbNoReply);
    }

    private void handlerSaveTemplateGlobalTts(Integer callTemplateId, TemplateGlobalTtsInfo tts) {
        TemplateGlobalTts dbTts = templateGlobalTtsService.findTemplateGlobalTtsByCallTemplateId(callTemplateId);
        dbTts.setEnableTts(tts.enabled()? YesNoEnum.YES.getCode(): YesNoEnum.NO.getCode());
        dbTts.setSpeed(tts.speed());
        dbTts.setVolume(tts.volume());
        dbTts.setPitch(tts.pitch());
        dbTts.setEngine(tts.engine());
        templateGlobalTtsService.updateTemplateGlobalTts(dbTts);
    }

    private void handlerSaveTemplateGlobalNlu(Integer callTemplateId, TemplateGlobalNluInfo nlu) {
        TemplateGlobalNlu dbNlu = templateGlobalNluService.findTemplateGlobalNluByCallTemplateId(callTemplateId);
        dbNlu.setEnableNlu(nlu.enabled()? YesNoEnum.YES.getCode(): YesNoEnum.NO.getCode());
        dbNlu.setThreshold(nlu.threshold());
        dbNlu.setModeId(nlu.modelName());
        templateGlobalNluService.updateTemplateGlobalNlu(dbNlu);
    }

    private void handlerSaveTemplateDefaultVerbal(Integer callTemplateId, TemplateGlobalDefaultVerbalInfo templateGlobalDefaultVerbalInfo) {
        TemplateGlobalDefaultVerbal defaultVerbal = templateGlobalDefaultVerbalService.findTemplateGlobalDefaultVerbalByCallTemplateId(callTemplateId);
        defaultVerbal.setEnableDefault(templateGlobalDefaultVerbalInfo.enabled()? YesNoEnum.YES.getCode(): YesNoEnum.NO.getCode());
        defaultVerbal.setDefaultAction(templateGlobalDefaultVerbalInfo.defaultAction());
        defaultVerbal.setTargetFlowId(templateGlobalDefaultVerbalInfo.targetFlowId());
        buildTemplateGlobalVerbal(defaultVerbal.getOrgId(), defaultVerbal.getCallTemplateId(), templateGlobalDefaultVerbalInfo.fileId(),defaultVerbal.getVerbalId(),templateGlobalDefaultVerbalInfo.verbalFileText());
        templateGlobalDefaultVerbalService.updateTemplateGlobalDefaultVerbal(defaultVerbal);
    }


    private void buildTemplateGlobalVerbal(Integer orgId,Integer callTemplateId, Integer fileId,Integer verbalId, String verbalContent) {
        String verbalFilePath = null;
        if (fileId!=null) {
            verbalFilePath = handlerSaveLocalTempFileToTemplate( orgId, callTemplateId, fileId,fileProperties.getPath().getBase(), templateConfig.getVerbalPath());
        }
        //更新新的话术内容和上传路径
        if (verbalFilePath!=null) {
            verbalRepository.updateTemplateVerbalFilePathById(verbalId,verbalContent,verbalFilePath);
        }else{
            verbalRepository.updateTemplateVerbalById(verbalId,verbalContent);
        }
    }

    private TemplateGlobalTtsInfo buildTemplateGlobalTtsInfo(TemplateGlobalTts globalTts) {
        if (globalTts == null) {
            return new TemplateGlobalTtsInfo(false, null,null,null,null,null);
        }
        return new TemplateGlobalTtsInfo(YesNoEnum.YES.getCode().equals(globalTts.getEnableTts()),
                globalTts.getEngine(), globalTts.getVoiceType(), globalTts.getSpeed(), globalTts.getVolume(), globalTts.getPitch());
    }

    @Override
    public LocalStorageRepository getSystemLocalStorageRepository() {
        return localStorageRepository;
    }

    @Override
    public TemplateVerbalRepository getVerbalRepository() {
        return verbalRepository;
    }
}
