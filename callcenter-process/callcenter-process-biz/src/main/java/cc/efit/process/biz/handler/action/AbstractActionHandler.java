package cc.efit.process.biz.handler.action;

import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.process.api.utils.ProcessChatLogFormatter;
import cc.efit.process.biz.base.CommonHandler;
import cc.efit.process.biz.handler.BaseActionHandler;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.handler.utils.TemplateCommonUtils;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisLock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cc.efit.process.api.constants.DialogueRedisConstant.DIALOGUE_SESSION_KEY;
import static cc.efit.process.api.constants.DialogueRedisConstant.DIALOGUE_SESSION_LOCK_KEY;

public abstract class AbstractActionHandler implements BaseActionHandler, CommonHandler {

    private static final int DIALOGUE_SESSION_LOCK_WAIT_SEC = 3;
    private static final int DIALOGUE_SESSION_LOCK_LEASE_SEC = 90;

    @Override
    public ChatProcessRes processActionHandle(ChatProcessReq req) {
        String callId = req.callId();
        if (callId == null || callId.isBlank()) {
            return BaseActionHandler.super.processActionHandle(req);
        }
        RedisLock redisLock = SpringBeanHolder.getBean(RedisLock.class);
        String lockKey = FormatUtil.formatTemplateRedisKey(DIALOGUE_SESSION_LOCK_KEY, callId);
        boolean locked = redisLock.tryLock(lockKey, DIALOGUE_SESSION_LOCK_WAIT_SEC, DIALOGUE_SESSION_LOCK_LEASE_SEC, TimeUnit.SECONDS);
        if (!locked) {
            logger.warn("对话会话锁竞争失败，callId:{}", callId);
            List<BaseActionData> actions = List.of(TemplateCommonUtils.buildHangupAction("系统繁忙请稍后"));
            ChatProcessRes res = new ChatProcessRes();
            res.setCallId(callId);
            res.setActions(actions);
            return res;
        }
        try {
            return BaseActionHandler.super.processActionHandle(req);
        } finally {
            redisLock.unlock(lockKey);
        }
    }

    @Override
    public DialogueProcessSession loadDialogueSession(String callId) {
        return getRedisObject(FormatUtil.formatTemplateRedisKey(DIALOGUE_SESSION_KEY, callId ), DialogueProcessSession.class);
    }

    @Override
    public void storeDialogueSession(String callId, DialogueProcessSession session) {
        if (session==null) {
            return;
        }
        setRedisJson(FormatUtil.formatTemplateRedisKey(DIALOGUE_SESSION_KEY, callId ), JsonUtils.toJsonString(session));
        logger.info("storeDialogueSession {}", ProcessChatLogFormatter.sessionBrief(session));
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
