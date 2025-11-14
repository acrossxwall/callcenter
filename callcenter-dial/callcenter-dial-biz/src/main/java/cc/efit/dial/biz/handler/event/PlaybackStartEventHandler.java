package cc.efit.dial.biz.handler.event;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.event.EventNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("playbackStartEventHandler")
@Slf4j
public class PlaybackStartEventHandler extends AbstractEventHandler {
    @Override
    public void eventHandler(DialProcessSession session, EslEvent event) throws ProcessActionException {
        String callId = session.getCallId();
        log.info("play start handler callId:{}",callId);
        session.setStatus(ProcessSessionStatusEnum.PLAYING);
        session.setPlayStartTime(LocalDateTime.now());
    }

    @Override
    public String getEventName() {
        return EventNames.PLAYBACK_START;
    }
}
