package cc.efit.dial.biz.handler.action;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.action.HangupActionData;
import cc.efit.process.api.enums.ProcessResActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("hangupAction")
public class HangupAction extends AbstractActionHandler {

    @Override
    protected String doAction(DialProcessSession session, BaseActionData actionData) throws ProcessActionException {
        String callId = session.getCallId();
        String callUuid = session.getCallUuid();
        log.info("callId:{},callUuid:{} ai hangup", callId, callUuid);
        session.setStatus(ProcessSessionStatusEnum.HANGUP);
        HangupActionData hangupAction = (HangupActionData) actionData;
        session.setHangupReason(hangupAction.getReason());
        hangupChannel(callUuid  );
        return null;
    }

    @Override
    public ProcessResActionEnum getActionEnum() {
        return ProcessResActionEnum.HANGUP;
    }
}
