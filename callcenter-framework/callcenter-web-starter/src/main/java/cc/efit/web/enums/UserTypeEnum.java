package cc.efit.web.enums;

public enum UserTypeEnum {
    ORG_ADMINISTRATOR(2, "租户管理员"),
    SYSTEM_ADMINISTRATOR(1, "系统管理员"),
    NORMAL(0, "普通用户");
    private Integer code;
    private String desc;
    UserTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }

    public static UserTypeEnum getUserTypeByCode(Integer code) {
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if (userTypeEnum.getCode().equals(code)) {
                return userTypeEnum;
            }
        }
        return NORMAL;
    }
}
