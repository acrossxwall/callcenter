package cc.efit.dial.biz.handler.status;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("playingStatusHandler")
@Slf4j
public class PlayingStatusHandler extends AbstractStatusHandler {
    @Override
    protected void doStatusHandler(DialProcessSession session) {
        String callId = session.getCallId();
        log.info("callId: {}, status: PLAYING", callId);
    }

    @Override
    public ProcessSessionStatusEnum currentStatus() {
        return ProcessSessionStatusEnum.PLAYING;
    }
}
