package cc.efit.dial.biz.handler.action;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.InteractiveRecordRoleEnum;
import cc.efit.dial.biz.handler.base.BaseActionHandler;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.res.MatchResult;

public abstract  class AbstractActionHandler implements BaseActionHandler {

    @Override
    public void actionProcess(DialProcessSession session, BaseActionData actionData, DialogueFlowData flowData,
                              MatchResult matchResult) throws ProcessActionException {
        //这里可以做一些公共处理，后续在加吧
        String content = doAction(session,actionData);
        if (content!=null) {
            //添加交互记录AI播音内容
            addInteractiveRecord(session.getCallId(), InteractiveRecordRoleEnum.AI, content,flowData,matchResult);
        }
    }

    protected abstract String doAction(DialProcessSession session, BaseActionData actionData) throws ProcessActionException;
}
