package cc.efit.process.biz.handler;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.core.DialogueProcessSession;

import java.util.List;

public interface BaseFlowHandler {

    List<BaseActionData> flowHandle(DialogueProcessSession session, ChatProcessReq req);
}
