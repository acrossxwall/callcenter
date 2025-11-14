package cc.efit.dial.biz.handler.base;

import cc.efit.esl.core.event.EslEvent;

public interface BaseEventHandler extends CommonHandler {

    void processEventHandler(String callId, EslEvent event);

    String getEventName();
}
