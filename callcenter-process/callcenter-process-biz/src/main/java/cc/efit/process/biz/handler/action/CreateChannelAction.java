package cc.efit.process.biz.handler.action;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.action.CreateChannelActionData;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.enums.flow.TemplateFlowStatusEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.core.DialogueProcessSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("createChannelAction")
@Slf4j
public class CreateChannelAction extends AbstractActionHandler {
    @Override
    public List<BaseActionData> actionHandle(DialogueProcessSession session, ChatProcessReq req) {
        String callId = req.callId();
        log.info("创建通道的 action:{}",  callId );
        if (session == null) {
            session = createDialogueSession(callId, req.callTemplateId());
        }
        session.setStatus(TemplateFlowStatusEnum.INIT);
        storeDialogueSession(callId,session);
        return List.of(new CreateChannelActionData());
    }

    @Override
    public Integer getActionIndex() {
        return ProcessReqActionEnum.CREATE.getCode();
    }
}
