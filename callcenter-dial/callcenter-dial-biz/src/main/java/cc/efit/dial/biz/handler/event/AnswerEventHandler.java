package cc.efit.dial.biz.handler.event;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.ActionHandlerFactory;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.dial.biz.service.ProcessService;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.event.EventNames;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("answerEventHandler")
@Slf4j
public class AnswerEventHandler extends AbstractEventHandler {
    @Autowired
    private ProcessService processService;
    @Autowired
    private ActionHandlerFactory actionHandlerFactory;
    @Override
    public void eventHandler(DialProcessSession session, EslEvent event) throws ProcessActionException {
        String callId = session.getCallId();
        log.info("answer event handler callId:{}",callId);
        session.setAsrResult(new ArrayList<>());
        session.setStatus(ProcessSessionStatusEnum.ANSWER);
        session.setAnswerTime(parseEventTime(event.getEventDateLocal()));
        ChatProcessReq req = buildChatProcessReq(session,"", ProcessReqActionEnum.START);
        ChatProcessRes res = processService.requestProcessChat(req);
        handlerAction( session, res);
        getFsApiCommand().startVoskAsr(session.getCallUuid());
    }

    @Override
    public String getEventName() {
        return EventNames.CHANNEL_ANSWER;
    }
}
