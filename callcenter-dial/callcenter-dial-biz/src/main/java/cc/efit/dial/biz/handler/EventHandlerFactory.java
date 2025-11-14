package cc.efit.dial.biz.handler;

import cc.efit.dial.biz.handler.base.BaseEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EventHandlerFactory {
    private Map<String, BaseEventHandler> eventHandlerMap = new HashMap<String, BaseEventHandler>();
    @Autowired
    public EventHandlerFactory(List<BaseEventHandler> eventHandlers){
        eventHandlerMap = eventHandlers.stream().collect(Collectors.toMap(BaseEventHandler::getEventName, e -> e));
    }

    public BaseEventHandler getEventHandler(String eventName){
        return eventHandlerMap.get(eventName);
    }
}
