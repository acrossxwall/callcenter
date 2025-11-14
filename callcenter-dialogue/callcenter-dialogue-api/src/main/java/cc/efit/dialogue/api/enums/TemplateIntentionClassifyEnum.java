package cc.efit.dialogue.api.enums;

public enum TemplateIntentionClassifyEnum {
    /** 意图分类属性 0 肯定  1 否定  2 拒绝  3 中性  4-其他 */
    POSITIVE(0, "肯定"),
    NEGATIVE(1, "否定"),
    REFUSE(2, "拒绝"),
    NEUTRAL(3, "中性"),
    OTHER(4, "其他");
    private Integer code;
    private String name;
    TemplateIntentionClassifyEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    public Integer getCode() {
        return code;
    }
}
