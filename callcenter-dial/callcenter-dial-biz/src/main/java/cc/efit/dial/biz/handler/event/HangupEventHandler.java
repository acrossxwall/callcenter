package cc.efit.dial.biz.handler.event;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.event.EventNames;
import cc.efit.json.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cc.efit.dial.api.constant.DialKeyConstants.DIAL_CALL_RECORD_QUEUE_KEY;

@Service("hangupEventHandler")
@Slf4j
public class HangupEventHandler extends AbstractEventHandler {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void eventHandler(DialProcessSession session, EslEvent event) throws ProcessActionException {
        String callId = session.getCallId();
        log.info("hangup event handler callId:{}",callId);
        session.setHangupTime(parseEventTime(event.getEventDateLocal()));
        //将session 通过mq发送的通话记录模块保存入库
        rabbitTemplate.convertAndSend(DIAL_CALL_RECORD_QUEUE_KEY, JsonUtils.toJsonString(session)) ;
    }

    @Override
    public String getEventName() {
        return EventNames.CHANNEL_HANGUP;
    }
}
