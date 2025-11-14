package cc.efit.process.api.enums;

public enum ProcessReqActionEnum {
    CREATE(1, "创建会话"),
    START(2, "开始会话"),
    CHAT(3, "聊天会话"),
    HANGUP(4, "挂断会话"),
    ;
    private Integer code;
    private String desc;
    ProcessReqActionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
}
