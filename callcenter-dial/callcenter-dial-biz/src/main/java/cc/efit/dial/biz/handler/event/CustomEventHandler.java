package cc.efit.dial.biz.handler.event;

import cc.efit.dial.api.core.DialProcessSession;
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

@Service("customEventHandler")
@Slf4j
public class CustomEventHandler extends AbstractEventHandler {
    @Autowired
    private ProcessService processService;

    @Override
    public void eventHandler(DialProcessSession session, EslEvent event) throws ProcessActionException {
        String callId = session.getCallId();
        log.info("custom event handler callId:{}",callId);
        //获取动作 根据不同的content
        ChatProcessReq req = buildChatProcessReq(session,"", ProcessReqActionEnum.START);
        ChatProcessRes res = processService.requestProcessChat(req);
        handlerAction( session, res);
    }

    @Override
    public String getEventName() {
        return EventNames.CUSTOM;
    }
}
