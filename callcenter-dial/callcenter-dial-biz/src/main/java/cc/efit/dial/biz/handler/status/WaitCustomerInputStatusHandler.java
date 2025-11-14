package cc.efit.dial.biz.handler.status;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.dial.biz.service.ProcessService;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import cc.efit.timer.core.TimerService;
import cc.efit.timer.exception.TimerExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static cc.efit.dial.api.constant.DialKeyConstants.DELAY_TASK_KEY;

@Service("waitCustomerInputStatusHandler")
@Slf4j
public class WaitCustomerInputStatusHandler extends AbstractStatusHandler {
    @Autowired
    private TimerService timerService;
    @Autowired
    private ProcessService processService;
    @Override
    protected void doStatusHandler(DialProcessSession session) throws ProcessActionException {
        String callId = session.getCallId();
        log.info("wait customer input status handler callId:{}", callId);
        if (session.isEnableNoReply()) {
            String delayTask = DELAY_TASK_KEY.formatted(callId);
            try {
                timerService.addDelayTask(delayTask, session.getNoReplySeconds(), TimeUnit.SECONDS, () -> {
                    handlerCustomerNoReply(session);
                },null);
            } catch (TimerExistsException e) {
                log.info("timer exists callId:{}", callId);
            }
        }
    }

    private void handlerCustomerNoReply(DialProcessSession session) {
        log.info("customer no reply callId:{}", session.getCallId());
        session.setStatus(ProcessSessionStatusEnum.WAIT_TIMEOUT);
        ChatProcessReq req = buildChatProcessReq(session,"", ProcessReqActionEnum.CHAT);
        ChatProcessRes res = processService.requestProcessChat(req);
        try {
            handlerAction( session, res);
        } catch (ProcessActionException e) {
            log.error("客户应答 process action error", e);
            hangupChannel(session.getCallUuid());
        }
    }

    @Override
    public ProcessSessionStatusEnum currentStatus() {
        return ProcessSessionStatusEnum.WAIT_CUSTOMER_INPUT;
    }
}
