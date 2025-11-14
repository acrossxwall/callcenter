package cc.efit.dispatch.biz.rule;

import cc.efit.dialogue.api.enums.RuleConditionEnum;
import cc.efit.dialogue.api.enums.RuleOperationEnum;
import cc.efit.dispatch.biz.rule.vo.RuleInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.api.core.InteractiveRecord;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface RuleHandler {

    /**
     * 处理规则
     * @param rule
     * @return
     */
    boolean handleRuleMatch(RuleInfo rule, DialogueProcessSession session, List<InteractiveRecord> list, int callDuration);

    RuleConditionEnum getCondition();

    default boolean ruleMatch(Integer val, RuleInfo rule) {
        if (val==null || StringUtils.isBlank(rule.value())) {
            return false;
        }
        Integer compareVal = Integer.valueOf(rule.value());
        RuleOperationEnum operation = RuleOperationEnum.getRuleOperationEnum(rule.operation());
        return switch (operation) {
            case GREATER_THAN_OR_EQUAL -> val >= compareVal;
            case EQUAL -> val.equals(compareVal);
            case LESS_THAN_OR_EQUAL -> val <= compareVal;
            case null -> false;
        };
    }
}
