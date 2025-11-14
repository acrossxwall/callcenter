package cc.efit.process.biz.handler.utils;

import cc.efit.core.enums.YesNoEnum;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.flow.TemplateFlowHangupModeEnum;
import cc.efit.process.api.enums.flow.TemplateFlowStatusEnum;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class TemplateFlowUtils {

    public static List<BaseActionData> buildTemplateFlowActionData(DialogueProcessSession session,  TemplateNodeInfo node, Map<String,String> customerInfo) {
        List<BaseActionData> actionDataList = new ArrayList<>();
        boolean playVerbal = true;
        if (YesNoEnum.YES.getCode().equals(node.getEnableHangup())
                && TemplateFlowHangupModeEnum.HANGUP.getCode().equals(node.getHangupMode())) {
            log.info("当前call id:{},模板id:{},节点:{}不回复话术直接挂机",session.getCallId(), node.getCallTemplateId(),node.getId() );
            playVerbal = false;
        }
        if (playVerbal) {
            List<BaseActionData > list = TemplateVerbalUtils.buildPlayVerbalContent(node.getVerbalList(), customerInfo, node.getVerbalType(),
                    node.getEnableInterrupt());
            if (!CollectionUtils.isEmpty(list))  {
                actionDataList.addAll(list);
            }
        }
        if (YesNoEnum.YES.getCode().equals(node.getEnableHangup())) {
            session.setStatus(TemplateFlowStatusEnum.HANGUP);
            session.setHangupTime(LocalDateTime.now());
            actionDataList.add(TemplateCommonUtils.buildHangupAction( ""));
        } else if (YesNoEnum.YES.getCode().equals(node.getEnableTransfer())) {
            session.setStatus(TemplateFlowStatusEnum.TRANSFER);
            session.setTransferTime(LocalDateTime.now());
            actionDataList.add(TemplateCommonUtils.buildTransferAction(  node.getTransferNumber(),node.getTransferType(), node.getTransferAgentGroupId() ));
        }
        //TODO 构建发送短信的判断信息
        return actionDataList;
    }

}
