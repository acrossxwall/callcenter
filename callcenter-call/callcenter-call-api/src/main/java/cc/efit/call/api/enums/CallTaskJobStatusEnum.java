package cc.efit.call.api.enums;

public enum CallTaskJobStatusEnum {
    /** 状态：1:成功，2:失败 */
    SUCCESS(1), FAIL(2);
    private int value;
    CallTaskJobStatusEnum(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
