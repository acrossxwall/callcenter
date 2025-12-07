package cc.efit.dial.biz.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.base.BaseStatusHandler;

@Component
public class StatusHandlerFactory {

    private Map<ProcessSessionStatusEnum, BaseStatusHandler> sessionStatusMap ;
    @Autowired
    public StatusHandlerFactory(List<BaseStatusHandler> statusHandlers){
        sessionStatusMap = statusHandlers.stream().collect(Collectors.toMap(s->s.currentStatus(), v->v));
    }

    public BaseStatusHandler getBaseStatusHandler(ProcessSessionStatusEnum sessionStatusEnum){
        if (sessionStatusEnum==null) {
            return null;
        }
        return sessionStatusMap.get(sessionStatusEnum);
    }
}
