package cc.efit.notify.core;

public record PushMessage (
    String title,
    String content,
    ChannelTypeEnum channelType
){

}
