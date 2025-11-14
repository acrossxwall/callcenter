package cc.efit.esl.core;

import cc.efit.esl.config.FreeswitchProperties;
import cc.efit.esl.inbound.Client;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

@Slf4j
public class FsClient extends Client  {
    private FreeswitchProperties freeswitchProperties;
    @Setter
    private SocketDisconnectListener disconnectListener;
    private static final ScheduledExecutorService executors =  Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> scheduledFuture;
    public FsClient(FreeswitchProperties freeswitchProperties){
        this.freeswitchProperties = freeswitchProperties;
    }

    @Override
    public void onChannelDisconnect() {
        if (this.isReconnect()) {
            log.info("channel  already reconnecting, ignore this event");
            return;
        }
        this.setReconnect(true);
        log.info("channel disconnect, reconnecting...");
        if (disconnectListener != null) {
            disconnectListener.onDisconnect();
        }
        scheduledFuture = executors.scheduleAtFixedRate(() -> {
            if (this.canSend()) {
                log.info("reconnect success");
                this.setReconnect(false);
                this.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, freeswitchProperties.getEventSubscriptions());
                scheduledFuture.cancel(true);
                return;
            }
            try {
                this.connect(new InetSocketAddress(freeswitchProperties.getHost(), freeswitchProperties.getPort()),
                        freeswitchProperties.getPassword(), freeswitchProperties.getTimeoutSeconds());
            }catch (Exception ignore){
                //忽略
            }
        },30,30, TimeUnit.SECONDS);
    }
}
