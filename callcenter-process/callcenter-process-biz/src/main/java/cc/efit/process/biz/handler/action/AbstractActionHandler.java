package cc.efit.process.biz.handler.action;

import cc.efit.process.api.utils.FormatUtil;
import cc.efit.process.biz.base.CommonHandler;
import cc.efit.process.biz.handler.BaseActionHandler;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.json.utils.JsonUtils;

import java.time.LocalDateTime;

import static cc.efit.process.api.constants.DialogueRedisConstant.DIALOGUE_SESSION_KEY;

public abstract class AbstractActionHandler implements BaseActionHandler, CommonHandler {

    @Override
    public DialogueProcessSession loadDialogueSession(String callId) {
        return getRedisObject(FormatUtil.formatTemplateRedisKey(DIALOGUE_SESSION_KEY, callId ), DialogueProcessSession.class);
    }

    @Override
    public void storeDialogueSession(String callId, DialogueProcessSession session) {
        if (session==null) {
            return;
        }
        String jsonSession = JsonUtils.toJsonString(session);
        logger.info("storeDialogueSession callId:{},session:{}", callId, jsonSession);
        setRedisJson(FormatUtil.formatTemplateRedisKey(DIALOGUE_SESSION_KEY, callId ), jsonSession);
    }

    protected DialogueProcessSession createDialogueSession(String callId, Integer callTemplateId) {
        DialogueProcessSession session = new DialogueProcessSession();
        session.setCallId(callId);
        session.setCallTemplateId(callTemplateId);
        session.setCallTime(LocalDateTime.now());
        session.setInteractiveCount(0);
        session.setNoReplyCount(0);
        session.setBusinessKnowledge(0);
        session.setGeneralKnowledge(0);
        session.setAffirmCount(0);
        session.setNegativeCount(0);
        session.setRefuseCount(0);
        session.setSilentCount(0);
        return session;
    }
}
