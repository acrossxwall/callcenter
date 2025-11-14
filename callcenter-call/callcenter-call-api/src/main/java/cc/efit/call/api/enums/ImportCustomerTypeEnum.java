package cc.efit.call.api.enums;

public enum ImportCustomerTypeEnum {
    /*  1-Excel文件导入 2-手工导入 */
    EXCEL(1, "Excel文件导入"),
    MANUAL(2, "手工导入");
    ;
    private Integer type;
    private String desc;
    ImportCustomerTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
}
