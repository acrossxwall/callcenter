package cc.efit.call.api.enums;

public enum CustomerBatchEnum {
    /** 状态 0-未导入 1-导入成功 2-部分成功 3-失败 */
     NOT_IMPORT(0, "未导入"),
     IMPORT_SUCCESS(1, "导入成功"),
     PART_IMPORT_SUCCESS(2, "部分成功"),
     IMPORT_FAIL(3, "失败");
    private int code;
    private String desc;
    CustomerBatchEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode() {
        return code;
    }
}
