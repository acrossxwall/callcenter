package cc.efit.enums;

public enum CommonOperatorEnum {
    ADD(1, "新增"),
    DELETE(2, "删除"),
    UPDATE(3, "修改"),
    ;
    private Integer code;
    private String desc;
    CommonOperatorEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
}
