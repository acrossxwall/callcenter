package cc.efit.process.api.enums.global;

public enum TemplateGlobalEnum {
    HANGUP(1,"是"),
    GOTO(0,"否");
    private Integer code;
    private String desc;
    TemplateGlobalEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
}
