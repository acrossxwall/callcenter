package cc.efit.dispatch.biz.call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CallTaskHandlerFactory {
    private final Map<Integer , CallTaskHandler> handlerMap  ;
    @Autowired
    public CallTaskHandlerFactory(List<CallTaskHandler> handlers){
        handlerMap = handlers.stream().collect(Collectors.toMap(s->s.getDispatchCallTaskTypeEnum().ordinal(),
                handler -> handler));
    }

    public CallTaskHandler getHandler(Integer type){
        return handlerMap.get(type);
    }
}
