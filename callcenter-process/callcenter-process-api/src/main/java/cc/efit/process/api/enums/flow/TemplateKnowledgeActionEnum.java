package cc.efit.process.api.enums.flow;

public enum TemplateKnowledgeActionEnum {
    /** 动作 1.回到跳出主流程节点 2.回到跳出主流程的上个节点 3.指定节点 4,转人工 5.挂断 */
    BACK_TO_MAIN_FLOW(1, "回到跳出主流程节点"),
    BACK_TO_MAIN_FLOW_PRE(2, "回到跳出主流程的上个节点"),
    SPECIFIED_NODE(3, "指定节点"),
    TRANSFER(4, "转人工"),
    HANG_UP(5, "挂断");
    private int code;
    private String desc;
    TemplateKnowledgeActionEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode() {
        return code;
    }
}
