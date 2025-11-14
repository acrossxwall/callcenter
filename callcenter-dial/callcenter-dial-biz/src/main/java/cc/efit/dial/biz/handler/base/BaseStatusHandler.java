package cc.efit.dial.biz.handler.base;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;

public interface BaseStatusHandler extends CommonHandler {


    void statusProcess(DialProcessSession session) throws ProcessActionException;

    ProcessSessionStatusEnum currentStatus();
}
