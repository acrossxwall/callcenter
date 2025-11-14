package cc.efit.process.biz.handler;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.handler.flow.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseFlowHandlerChain {

    private BaseFlowHandler firstHandler;
    @Autowired
    public BaseFlowHandlerChain(GlobalInteractionHandler globalInteractionHandler,
                                GlobalNoReplyHandler globalNoReplyHandler,
                                JumpFlowHandler jumpFlowHandler,
                                PredictFlowHandler predictFlowHandler, TargetFlowHandler targetFlowHandler,
                                KnowledgeHandler knowledgeHandler, GlobalDefaultVerbalHandler globalDefaultVerbalHandler) {
        globalInteractionHandler.setNextHandler(globalNoReplyHandler);
        globalNoReplyHandler.setNextHandler(predictFlowHandler);
        predictFlowHandler.setNextHandler(jumpFlowHandler);
        jumpFlowHandler.setNextHandler(targetFlowHandler);
        targetFlowHandler.setNextHandler(knowledgeHandler);
        knowledgeHandler.setNextHandler(globalDefaultVerbalHandler);
        this.firstHandler = globalInteractionHandler;
    }

    public List<BaseActionData> flowHandleChain(DialogueProcessSession session, ChatProcessReq req) {
        return firstHandler.flowHandle(session, req);
    }
}
