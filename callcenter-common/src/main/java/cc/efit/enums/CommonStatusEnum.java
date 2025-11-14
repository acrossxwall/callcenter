package cc.efit.enums;

public enum CommonStatusEnum {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private Integer code;
    private String desc;
    CommonStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
}
