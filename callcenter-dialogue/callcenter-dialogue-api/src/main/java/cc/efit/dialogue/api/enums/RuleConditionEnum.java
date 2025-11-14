package cc.efit.dialogue.api.enums;

public enum RuleConditionEnum {
//    {"label":"交互轮次","value":0},
//    {"label":"触发业务问题次数","value":1},
//    {"label":"触发肯定次数","value":2},
//    {"label":"触发否定次数","value":3},
//    {"label":"触发拒绝次数","value":4},
//    {"label":"用户静默次数","value":5},
//    {"label":"通话时长","value":6},
//    {"label":"触发知识库","value":7},
//    {"label":"触发节点标签","value":8},
    INTERACTION_ROUND(0,"交互轮次"),
    BUSINESS_QUESTION_COUNT(1,"触发业务问题次数"),
    AFFIRM_COUNT(2,"触发肯定次数"),
    NEGATIVE_COUNT(3,"触发否定次数"),
    REFUSE_COUNT(4,"触发拒绝次数"),
    USER_SILENT_COUNT(5,"用户静默次数"),
    CALL_DURATION(6,"通话时长"),
    TRIGGER_KNOWLEDGE(7,"触发知识库"),
    TRIGGER_NODE_TAG(8,"触发节点标签");
    private Integer value;
    private String label;
    RuleConditionEnum(Integer value,String label){
        this.value=value;
        this.label=label;
    }
    public Integer getValue() {
        return value;
    }
}
