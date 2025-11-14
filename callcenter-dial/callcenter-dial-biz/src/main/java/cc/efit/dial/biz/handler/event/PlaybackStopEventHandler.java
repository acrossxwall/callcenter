package cc.efit.dial.biz.handler.event;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.event.EventNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("playbackStopEventHandler")
@Slf4j
public class PlaybackStopEventHandler extends AbstractEventHandler {
    @Override
    public void eventHandler(DialProcessSession session, EslEvent event) throws ProcessActionException {
        String callId = session.getCallId();
        log.info("play stop handler callId:{}",callId);
        //播音结束等待客户输入
        session.setStatus(ProcessSessionStatusEnum.WAIT_CUSTOMER_INPUT);
    }

    @Override
    public String getEventName() {
        return EventNames.PLAYBACK_STOP;
    }
}
