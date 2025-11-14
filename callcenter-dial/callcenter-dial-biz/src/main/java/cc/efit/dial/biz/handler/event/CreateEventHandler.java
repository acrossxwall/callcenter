package cc.efit.dial.biz.handler.event;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.service.ProcessService;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.event.EventNames;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.req.ChatProcessReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("createEventHandler")
@Slf4j
public class CreateEventHandler extends AbstractEventHandler {
    @Autowired
    private ProcessService processService;

    @Override
    public void eventHandler(DialProcessSession session, EslEvent event) {
        String callId = session.getCallId();
        log.info("create event handler callId:{}",callId);
        String eventDate = event.getEventDateLocal();
        session.setCallTime(parseEventTime(eventDate));
        session.setStatus(ProcessSessionStatusEnum.CREATED);
        ChatProcessReq req = buildChatProcessReq(session,"", ProcessReqActionEnum.CREATE);
        processService.requestProcessChat(req);
    }

    @Override
    public String getEventName() {
        return EventNames.CHANNEL_CREATE;
    }
}
