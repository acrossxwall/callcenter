package cc.efit.notify.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cc.efit.notify.PushChannel;

public abstract class AbstractPushChannel implements PushChannel {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public PushResult pushNotify(PushMessage pushMessage) {
        prePushHandler(pushMessage);
        String channelName = getChannelType().getName();
        boolean success ;
        String message = "";
        try {
            message = doPush(message);
            success = !StringUtils.hasLength(message);
        } catch (Exception e) {
            success = false;
        }
        return new PushResult(success, message, channelName);
    }

    protected abstract String doPush(String message);

    protected void prePushHandler(PushMessage pushMessage) {
        logger.info("start push message:{}, channel:{}",pushMessage, getChannelType());
    }

    
}
