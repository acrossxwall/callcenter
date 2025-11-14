package cc.efit.process.api.enums.flow;

public enum TemplateFlowHangupModeEnum {
//     0直接挂机: 1:回复话术后挂机
    HANGUP(0, "直接挂机"), HANGUP_AFTER_REPLY(1, "回复话术后挂机");
    private Integer code;
    private String desc;
    TemplateFlowHangupModeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
    public static TemplateFlowHangupModeEnum getHangupModeEnum(Integer code) {
        for (TemplateFlowHangupModeEnum hangupModeEnum : TemplateFlowHangupModeEnum.values()) {
            if (hangupModeEnum.getCode().equals(code)) {
                return hangupModeEnum;
            }
        }
        return HANGUP_AFTER_REPLY;
    }
}
