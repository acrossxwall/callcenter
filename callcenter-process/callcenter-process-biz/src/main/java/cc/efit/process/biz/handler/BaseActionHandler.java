package cc.efit.process.biz.handler;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.handler.utils.TemplateCommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public interface BaseActionHandler {

    Logger logger = LoggerFactory.getLogger(BaseActionHandler.class);

    default ChatProcessRes  processActionHandle(ChatProcessReq req) {
        //先加载session
        DialogueProcessSession session = loadDialogueSession(req.callId());
        //执行action 处理方法
        List<BaseActionData> actions ;
        try {
            actions = actionHandle(session, req);
            logger.info("当前callId:{},返回信息:{}", req.callId(),actions);
        }catch (Exception e) {
            logger.error("actionHandle error", e);
            actions = buildHangupAction(session ,"系统异常");
        }

        ChatProcessRes res = buildProcessRes(session, req.callId(), actions);
        //清理session 中间值
        clearSessionTempInfo(session);
        //保存session 到redis
        storeDialogueSession(req.callId(), session);
        return  res;
    }

    default void clearSessionTempInfo(DialogueProcessSession session){
        if (session==null) {
            return;
        }
        session.setTargetFlowId(null);
        session.setKeywordIntentionIds(null);
        session.setMatchResult(null);
        session.setFlowData(null);
        session.setMatchKnowledge(false);
    }

    default  List<BaseActionData> buildHangupAction(DialogueProcessSession session,  String reason) {
        if (session!=null) {
            session.setHangupTime(LocalDateTime.now());
        }
        return List.of(TemplateCommonUtils.buildHangupAction( reason));
    }
    default ChatProcessRes buildProcessRes(DialogueProcessSession session, String callId, List<BaseActionData> actions) {
        ChatProcessRes res = new ChatProcessRes();
        res.setCallId(callId);
        res.setActions(actions);
        if (session!=null) {
            res.setFlowData( session.getFlowData());
            res.setMatchResult(session.getMatchResult());
        }
        return res;
    }

    DialogueProcessSession loadDialogueSession(String callId);
    void storeDialogueSession(String callId,DialogueProcessSession session);

    List<BaseActionData> actionHandle(DialogueProcessSession session, ChatProcessReq req);
    Integer getActionIndex();
}
