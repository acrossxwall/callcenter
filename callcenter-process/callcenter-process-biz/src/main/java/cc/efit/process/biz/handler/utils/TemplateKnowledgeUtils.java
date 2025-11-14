package cc.efit.process.biz.handler.utils;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.flow.TemplateFlowStatusEnum;
import cc.efit.process.api.enums.flow.TemplateKnowledgeActionEnum;
import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.process.api.core.DialogueProcessSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
public class TemplateKnowledgeUtils {

    public static List<BaseActionData> buildTemplateKnowledgeActionData(DialogueProcessSession session, TemplateKnowledgeVo knowledgeVo, Map<String,String> customerInfo) {
        List<BaseActionData> actionDataList = new ArrayList<>();
        List<BaseActionData > list = TemplateVerbalUtils.buildPlayVerbalContent(knowledgeVo.getVerbalList(), customerInfo, null,
                knowledgeVo.getEnableInterrupt());
        if (!CollectionUtils.isEmpty(list))  {
            actionDataList.addAll(list);
        }
        if (TemplateKnowledgeActionEnum.HANG_UP.getCode() == knowledgeVo.getAction()) {
            session.setStatus(TemplateFlowStatusEnum.HANGUP);
            session.setHangupTime(LocalDateTime.now());
            actionDataList.add(TemplateCommonUtils.buildHangupAction( ""));
        } else if (TemplateKnowledgeActionEnum.TRANSFER.getCode() == knowledgeVo.getAction()) {
            session.setStatus(TemplateFlowStatusEnum.TRANSFER);
            session.setTransferTime(LocalDateTime.now());
            actionDataList.add(TemplateCommonUtils.buildTransferAction(  knowledgeVo.getTransferNumber(), knowledgeVo.getTransferType(), knowledgeVo.getAgentGroupId() ));
        }
        //TODO 构建发送短信的判断信息
        return actionDataList;
    }
}
