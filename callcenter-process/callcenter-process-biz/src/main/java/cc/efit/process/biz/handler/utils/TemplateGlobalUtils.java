package cc.efit.process.biz.handler.utils;

import cc.efit.dialogue.api.vo.global.TemplateGlobalDefaultVerbalInfo;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.flow.TemplateFlowStatusEnum;
import cc.efit.process.api.enums.global.TemplateGlobalEnum;
import cc.efit.dialogue.api.vo.global.TemplateGlobalInteractionInfo;
import cc.efit.dialogue.api.vo.global.TemplateGlobalNoReplyInfo;
import cc.efit.dialogue.api.vo.verbal.TemplateVerbalContentVo;
import cc.efit.process.api.core.DialogueProcessSession;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateGlobalUtils {

    public static List<BaseActionData> buildTemplateGlobalActionData(DialogueProcessSession session,
                                                                   TemplateGlobalInteractionInfo info,
                                                                   Map<String,String> customerInfo) {
        List<BaseActionData> actionDataList = new ArrayList<>();
        boolean playVerbal = StringUtils.isNotBlank(info.verbalFileText());
        if (playVerbal) {
            BaseActionData playActionData = TemplateVerbalUtils.buildPlayActionData( customerInfo,
                    new TemplateVerbalContentVo(null, info.verbalFilePath(),info.verbalFileText(),null,null),
                    null);
            if (playActionData!=null)  {
                actionDataList.add (playActionData);
            }
        }
        BaseActionData hangup =  buildGlobalAction(info.interactionAction(), session,  info.targetFlowId());
        if (hangup!=null) {
            actionDataList.add(hangup);
        }
        return actionDataList;
    }

    public static List<BaseActionData> buildTemplateNoReplyActionData(DialogueProcessSession session,
                                                                      TemplateGlobalNoReplyInfo info,
                                                                      Map<String,String> customerInfo) {
        List<BaseActionData> actionDataList = new ArrayList<>();
        boolean playVerbal = StringUtils.isNotBlank(info.verbalFileText());
        if (playVerbal) {
            BaseActionData playActionData = TemplateVerbalUtils.buildPlayActionData( customerInfo,
                    new TemplateVerbalContentVo(null, info.verbalFilePath(),info.verbalFileText(),null,null),
                    null);
            if (playActionData!=null)  {
                actionDataList.add (playActionData);
            }
        }
        BaseActionData hangup = buildGlobalAction(info.noreplyAction(), session,   info.targetFlowId());
        if (hangup!=null) {
            actionDataList.add(hangup);
        }
        return actionDataList;
    }

    public static List<BaseActionData> buildTemplateDefaultVerbalActionData(DialogueProcessSession session,
                                                                      TemplateGlobalDefaultVerbalInfo info,
                                                                      Map<String,String> customerInfo) {
        List<BaseActionData> actionDataList = new ArrayList<>();
        boolean playVerbal = StringUtils.isNotBlank(info.verbalFileText());
        if (playVerbal) {
            BaseActionData playActionData = TemplateVerbalUtils.buildPlayActionData( customerInfo,
                    new TemplateVerbalContentVo(null, info.verbalFilePath(),info.verbalFileText(),null,null),
                    null);
            if (playActionData!=null)  {
                actionDataList.add (playActionData);
            }
        }
        BaseActionData hangup = buildGlobalAction(info.defaultAction(), session,   info.targetFlowId());
        if (hangup!=null) {
            actionDataList.add(hangup);
        }
        return actionDataList;
    }

    private static BaseActionData buildGlobalAction(Integer action, DialogueProcessSession session,Integer targetFlowId) {
        if (TemplateGlobalEnum.HANGUP.getCode().equals(action)) {
            //挂断
            session.setStatus(TemplateFlowStatusEnum.HANGUP);
            session.setHangupTime(LocalDateTime.now());
            return TemplateCommonUtils.buildHangupAction("") ;
        } else {
            //不做其他处理，当前会话跳转到指定节点
            session.setFlowId(targetFlowId);
        }
        return null;
    }
}
