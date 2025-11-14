package cc.efit.process.biz.handler.flow;

import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.dialogue.api.constants.TemplateConstants;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.MatchResult;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.biz.base.CommonHandler;
import cc.efit.process.biz.handler.BaseFlowHandler;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.predict.BasePredictHandler;
import cc.efit.process.biz.predict.PredictHandlerFactory;
import lombok.Setter;

import java.util.List;

@Setter
public abstract class AbstractFlowHandler implements BaseFlowHandler, CommonHandler {
    private BaseFlowHandler nextHandler;

    @Override
    public List<BaseActionData> flowHandle(DialogueProcessSession session, ChatProcessReq req) {
        List<BaseActionData> actions = handler(session, req);
        if (actions!=null && !actions.isEmpty()) {
            return actions;
        }
        return nextHandler==null?null:nextHandler.flowHandle(session, req);
    }

    public Integer predictFlow(String content, DialogueProcessSession session,
                               TemplateNodeInfo flowNode) {
        PredictHandlerFactory predictFactory = SpringBeanHolder.getBean(PredictHandlerFactory.class);
        //先进行keyword预测
        BasePredictHandler handler = predictFactory.getPredictHandler(TemplateConstants.PREDICT_TYPE_KEYWORD);
        MatchResult matchResult = handler.predictFlow(content, flowNode,session);
        if (matchResult!=null ) {
            session.setMatchResult(matchResult);
            return matchResult.flowId();
        }
        handler = predictFactory.getPredictHandler(TemplateConstants.PREDICT_TYPE_NLU);
        matchResult = handler.predictFlow(content, flowNode,session);
        if (matchResult!=null) {
            session.setMatchResult(matchResult);
            return matchResult.flowId();
        }
        return null;
    }

    public abstract List<BaseActionData> handler(DialogueProcessSession session, ChatProcessReq req) ;
}
