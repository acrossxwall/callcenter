package cc.efit.dial.biz.handler;

import cc.efit.dial.biz.handler.base.BaseActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ActionHandlerFactory {

    private Map<Integer, BaseActionHandler> actionHandlerMap = new HashMap<>();
    @Autowired
    public ActionHandlerFactory(List<BaseActionHandler> actionHandlers){
        actionHandlerMap = actionHandlers.stream().collect(Collectors.toMap(
                a->a.getActionEnum().getAction(), a -> a
        ));
    }
    public BaseActionHandler getActionHandler(int action){
        return actionHandlerMap.get(action);
    }
}
