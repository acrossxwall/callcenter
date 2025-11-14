package cc.efit.dialogue.api.enums;

public enum TemplateFlowTypeEnum {
    START(1, "开场白"),
    NORMAL(2, "普通节点"),
    JUMP(3, "跳转节点"),
    ;
    private Integer code;
    private String type;
    TemplateFlowTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }
    public Integer getCode() {
        return code;
    }
    public static String getTypeByCode(Integer code) {
        for (TemplateFlowTypeEnum flowType : TemplateFlowTypeEnum.values()) {
            if (flowType.getCode().equals(code)) {
                return flowType.type;
            }
        }
        return NORMAL.type;
    }
}
