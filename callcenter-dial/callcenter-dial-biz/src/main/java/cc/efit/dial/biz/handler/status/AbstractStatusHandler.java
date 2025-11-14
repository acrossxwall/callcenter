package cc.efit.dial.biz.handler.status;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.biz.handler.base.BaseStatusHandler;
import cc.efit.dial.biz.handler.exception.ProcessActionException;

public abstract class AbstractStatusHandler implements BaseStatusHandler {
    @Override
    public void statusProcess(DialProcessSession session) throws ProcessActionException {
        //这个地方可以做公共处理
        doStatusHandler(session);
    }

    protected abstract void doStatusHandler(DialProcessSession session) throws ProcessActionException;
}
