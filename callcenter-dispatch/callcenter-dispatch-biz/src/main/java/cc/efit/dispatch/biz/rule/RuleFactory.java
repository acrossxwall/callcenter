package cc.efit.dispatch.biz.rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RuleFactory {

    private Map<Integer ,RuleHandler> ruleHandlerMap=new HashMap<>();
    @Autowired
    public RuleFactory (List<RuleHandler> ruleHandlers){
        ruleHandlerMap = ruleHandlers.stream().collect(Collectors.toMap(s->s.getCondition().getValue(),s->s));
    }

    public RuleHandler getRuleHandler(Integer condition){
        return ruleHandlerMap.get(condition);
    }
}
