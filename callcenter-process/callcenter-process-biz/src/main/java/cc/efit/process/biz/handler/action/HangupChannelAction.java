package cc.efit.process.biz.handler.action;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.enums.flow.TemplateFlowStatusEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.core.DialogueProcessSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("hangupChannelAction")
@Slf4j
public class HangupChannelAction  extends AbstractActionHandler {
    @Override
    public List<BaseActionData> actionHandle(DialogueProcessSession session, ChatProcessReq req) {
        log.info("callId:{},hangup 处理相关资源", session.getCallId());
        session.setHangupTime(LocalDateTime.now());
        session.setStatus(TemplateFlowStatusEnum.HANGUP);
        return null;
    }

    @Override
    public Integer getActionIndex() {
        return ProcessReqActionEnum.HANGUP.getCode();
    }
}
