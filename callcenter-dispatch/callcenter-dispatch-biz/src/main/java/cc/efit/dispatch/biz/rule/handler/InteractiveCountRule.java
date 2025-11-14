package cc.efit.dispatch.biz.rule.handler;

import cc.efit.dialogue.api.enums.RuleConditionEnum;
import cc.efit.dispatch.biz.rule.RuleHandler;
import cc.efit.dispatch.biz.rule.vo.RuleInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.api.core.InteractiveRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class InteractiveCountRule implements RuleHandler {
    @Override
    public boolean handleRuleMatch(RuleInfo rule, DialogueProcessSession session, List<InteractiveRecord> list, int callDuration) {
        String callId = session.getCallId();
        log.info("callId 交互轮次规则匹配开始:{}", callId);
        Integer interactiveCount = session.getInteractiveCount();
        return ruleMatch(interactiveCount, rule);
    }

    @Override
    public RuleConditionEnum getCondition() {
        return RuleConditionEnum.INTERACTION_ROUND;
    }
}
