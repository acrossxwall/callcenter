package cc.efit.dial.biz.handler.action;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.ProcessResActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("createChannelAction")
@Slf4j
public class CreateChannelAction extends AbstractActionHandler {
    @Override
    protected String doAction(DialProcessSession session, BaseActionData actionData) {
        String callId = session.getCallId();
        //创建拨打交互通道 这个估计是用不到，创建完通道不需要处理
        log.info("创建拨打通道处理，callId:{}", callId);
        return null;
    }

    @Override
    public ProcessResActionEnum getActionEnum() {
        return ProcessResActionEnum.CREATE_CHANNEL;
    }
}
