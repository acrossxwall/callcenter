package cc.efit.dial.biz.handler.event;

import cc.efit.utils.DateUtil;
import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.dial.api.constant.DialKeyConstants;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.client.FsApiCommand;
import cc.efit.dial.biz.handler.base.BaseEventHandler;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;

import static cc.efit.dial.biz.constants.DialConstants.CALL_UUID_CALL_ID_MAP;

public abstract class AbstractEventHandler implements BaseEventHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void processEventHandler(String callId, EslEvent event) {
        DialProcessSession session = loadSessionFromRedis(callId);
        if (session.getCallUuid()==null) {
            session.setCallUuid(event.getCallerUuid());
            //有的event 没有自定义callId ，所以将二者缓存起来
            CALL_UUID_CALL_ID_MAP.put(event.getCallerUuid(),callId);
        }
        if (session.getStatus()== ProcessSessionStatusEnum.HANGUP || session.getStatus()== ProcessSessionStatusEnum.TRANSFER_AGENT) {
            //转人工的待定，应该可能有其他事件要处理
            log.info("会话已结束或转人工callId:{},callUuid:{}，不处理事件", callId, event.getCallerUuid());
            return;
        }
        try {
            eventHandler(session, event);
        } catch (ProcessActionException e) {
            //处理了异常
            log.error("处理事件异常",e);
            hangupChannel(callId, event.getCallerUuid());
        }
        storeSessionToRedis(session);
    }

    private void hangupChannel(String callId,String callerUuid) {
        log.info("抛出异常挂断通道:{},{}",callId, callerUuid);
        FsApiCommand fsApiCommand = SpringBeanHolder.getBean(FsApiCommand.class);
        fsApiCommand.hangupChannel(callerUuid);
    }

    private void storeSessionToRedis(DialProcessSession session) {
        RedisUtils redisUtils = SpringBeanHolder.getBean(RedisUtils.class);
        redisUtils.set(DialKeyConstants.DIAL_SESSION_KEY.formatted(session.getCallId()), JsonUtils.toJsonString(session));
    }

    public abstract void eventHandler(DialProcessSession session,EslEvent event) throws  ProcessActionException;

    private DialProcessSession loadSessionFromRedis(String callId) {
        return getRedisUtils().get(DialKeyConstants.DIAL_SESSION_KEY.formatted(callId), DialProcessSession.class);
    }

    protected LocalDateTime parseEventTime(String eventTime) {
        if (!StringUtils.hasText(eventTime)) {
            return null;
        }
        return DateUtil.parseLocalDateTimeFormatyMdHms(eventTime);
    }

}
