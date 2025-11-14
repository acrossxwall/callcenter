package cc.efit.dial.biz.handler.base;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.ProcessResActionEnum;
import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.res.MatchResult;

public interface BaseActionHandler extends CommonHandler {

    void actionProcess(DialProcessSession session,
                       BaseActionData actionData, DialogueFlowData flowData, MatchResult matchResult) throws ProcessActionException;

    ProcessResActionEnum getActionEnum();
}
