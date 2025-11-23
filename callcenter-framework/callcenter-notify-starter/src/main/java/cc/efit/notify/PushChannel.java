package cc.efit.notify;

import cc.efit.notify.core.ChannelTypeEnum;
import cc.efit.notify.core.PushMessage;
import cc.efit.notify.core.PushResult;

public interface PushChannel {

    PushResult pushNotify(PushMessage pushMessage);

    ChannelTypeEnum getChannelType();
}
