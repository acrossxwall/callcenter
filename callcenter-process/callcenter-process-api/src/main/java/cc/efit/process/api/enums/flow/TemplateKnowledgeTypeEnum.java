package cc.efit.process.api.enums.flow;

public enum TemplateKnowledgeTypeEnum {

    /** 1:业务问题,2:一般问题 */
    BUSINESS(1, "业务问题"),
    GENERAL(2, "一般问题"),
    ;
    private Integer type;
    private String desc;
    TemplateKnowledgeTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
}
