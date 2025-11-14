package cc.efit.core.enums;

public enum YesNoEnum {
    YES(1, "是"),
    NO(0, "否");
    private Integer code;
    private String desc;
    YesNoEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
