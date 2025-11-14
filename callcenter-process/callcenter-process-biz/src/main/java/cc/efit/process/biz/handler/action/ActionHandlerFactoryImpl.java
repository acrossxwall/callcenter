package cc.efit.process.biz.handler.action;

import cc.efit.process.biz.handler.ActionHandlerFactory;
import cc.efit.process.biz.handler.BaseActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActionHandlerFactoryImpl implements ActionHandlerFactory {

    private final Map<Integer, BaseActionHandler> handlers;

    @Autowired
    public ActionHandlerFactoryImpl(List<BaseActionHandler> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(BaseActionHandler::getActionIndex, handler -> handler));
    }
    @Override
    public BaseActionHandler getHandler(Integer action) {
        return handlers.get(action);
    }
}
