package cc.efit.process.api.action;

import cc.efit.process.api.enums.ProcessResActionEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author across
 * @Description
 * @Date 2025-08-30 10:07
 */
public final class CreateChannelActionData extends BaseActionData {


    public CreateChannelActionData( ) {
        super(ProcessResActionEnum.CREATE_CHANNEL);
    }
    //可以不做任务处理，也可以返回 asr 和 tts的配置信息 等待拨打电话时在处理
}
