package cc.efit.dial.biz.handler.action;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.action.TransferActionData;
import cc.efit.process.api.enums.ProcessResActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("transferAgentAction")
@Slf4j
public class TransferAgentAction extends AbstractActionHandler {
    @Override
    protected String doAction(DialProcessSession session, BaseActionData actionData) throws ProcessActionException {
        String callId = session.getCallId();
        String callUuid = session.getCallUuid();
        session.setStatus(ProcessSessionStatusEnum.PLAYING);
        TransferActionData transferAction = (TransferActionData) actionData;
        log.info("callId:{},callUuid:{},transfer agent:{}", callId, callUuid, transferAction.getType());
        //TODO 转接坐席 待实现 根据不同agent type类型 调用不同接口
        return null;
    }

    @Override
    public ProcessResActionEnum getActionEnum() {
        return ProcessResActionEnum.TRANSFER_AGENT;
    }
}
