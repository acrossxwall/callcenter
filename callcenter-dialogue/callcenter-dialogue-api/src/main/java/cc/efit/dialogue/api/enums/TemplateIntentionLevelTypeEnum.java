package cc.efit.dialogue.api.enums;

public enum TemplateIntentionLevelTypeEnum {
    NORMAL(0, "普通"),
    DEFAULT(1, "默认"),
    ;
    private Integer type;
    private String desc;
    TemplateIntentionLevelTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
}
