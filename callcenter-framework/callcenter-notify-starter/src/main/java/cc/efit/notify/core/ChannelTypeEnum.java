package cc.efit.notify.core;

public enum ChannelTypeEnum {

    DING_TALK("dingding"),
    WECHAT_WORK("wechat"),
    SMS("sms"),
    EMAIL("email");

    private String name;

    private ChannelTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
