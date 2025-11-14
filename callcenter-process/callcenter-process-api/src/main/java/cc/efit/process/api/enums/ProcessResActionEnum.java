package cc.efit.process.api.enums;

public enum ProcessResActionEnum {
    CREATE_CHANNEL(0, "创建通道"),
    PLAY_FILE(1, "播音文件"),
    PLAY_TTS(2, "播音tts"),
    TRANSFER_AGENT(3, "转人工"),
    HANGUP(4, "挂断"),
    ;
    private int action;
    private String desc;
    ProcessResActionEnum(int action, String desc) {
        this.action = action;
        this.desc = desc;
    }
    public int getAction(){
        return action;
    }

    public static ProcessResActionEnum getEnumByAction(int action){
        for (ProcessResActionEnum processResActionEnum : ProcessResActionEnum.values()) {
            if (processResActionEnum.getAction() == action) {
                return processResActionEnum;
            }
        }
        return null;
    }
}
