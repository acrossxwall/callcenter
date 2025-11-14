package cc.efit.call.api.enums;

public enum CustomerSourceEnum {
    /** 来源，1:页面导入，2:api导入 */
    PAGE(1, "页面导入"), API(2, "api导入");
    private int code;
    private String desc;
    CustomerSourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode() {
        return code;
    }
}
