package cc.efit.dialogue.api.enums;

public enum RuleOperationEnum {
//    {"label":">=","value":1},
//    {"label":"=","value":0},
//    {"label":"<=","value":-1}
    GREATER_THAN_OR_EQUAL(1),
    EQUAL(0),
    LESS_THAN_OR_EQUAL(-1);
    private Integer value;
    RuleOperationEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static RuleOperationEnum getRuleOperationEnum(Integer value) {
        for (RuleOperationEnum ruleOperationEnum : RuleOperationEnum.values()) {
            if (ruleOperationEnum.getValue().equals(value)) {
                return ruleOperationEnum;
            }
        }
        return null;
    }
}
