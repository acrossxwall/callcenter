package cc.efit.dialogue.api.enums;

public enum TemplateIntentionTypeEnum {
    NORMAL(0, "普通"),
    DEFAULT(1, "默认"),
    KNOWLEDGE(2,"知识库")
    ;
    private Integer code;
    private String desc;
    TemplateIntentionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
}
