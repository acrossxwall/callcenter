package cc.efit.process.biz.predict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PredictHandlerFactory {

    private Map<String, BasePredictHandler> handlerMap = new HashMap<>();

    @Autowired
    public PredictHandlerFactory(List<BasePredictHandler> handlers) {
        handlerMap = handlers.stream().collect(Collectors.toMap(BasePredictHandler::getHandlerType, h -> h));
    }

    public BasePredictHandler getPredictHandler(String type) {
        return handlerMap.get(type);
    }
}
