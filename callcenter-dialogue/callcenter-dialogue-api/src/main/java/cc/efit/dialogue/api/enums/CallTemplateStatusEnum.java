package cc.efit.dialogue.api.enums;

/**
 * @author across
 * @Description
 * @Date 2025-08-10 11:18
 */
public enum CallTemplateStatusEnum {
    EDIT(1, "编辑中"),
    CHECK(2, "审核中"),
    PASS(3, "审核通过"),
    REJECT(4, "审核不通过"),
    ;
    private Integer status;
    private String desc;
    CallTemplateStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public Integer getStatus() {
        return status;
    }
}
