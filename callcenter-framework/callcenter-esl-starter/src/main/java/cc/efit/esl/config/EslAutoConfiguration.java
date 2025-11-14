package cc.efit.esl.config;

import cc.efit.core.thread.CustomerThreadFactory;
import cc.efit.esl.core.FsClient;
import cc.efit.esl.core.IEslEventListener;
import cc.efit.esl.core.IModEslApi;
import cc.efit.esl.core.SocketDisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

@AutoConfiguration
@EnableConfigurationProperties({FreeswitchProperties.class, InboundCallbackProperties.class})
@ConditionalOnMissingBean(FsClient.class)
public class EslAutoConfiguration {
    @Autowired
    private InboundCallbackProperties inboundCallbackProperties;
    @Autowired(required = false)
    private SocketDisconnectListener disconnectListener;
    @Bean
    public FsClient createFsClient(FreeswitchProperties freeswitchProperties, IEslEventListener listener) {
        FsClient fsClient = new FsClient(freeswitchProperties);
        fsClient.connect(new InetSocketAddress(freeswitchProperties.getHost(), freeswitchProperties.getPort()),
                freeswitchProperties.getPassword(), freeswitchProperties.getTimeoutSeconds());
        fsClient.addEventListener(listener);
        fsClient.setDisconnectListener(disconnectListener);
        fsClient.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, freeswitchProperties.getEventSubscriptions());
        fsClient.setCallbackExecutor(threadPoolTaskExecutor());
        return fsClient;
    }

    @Bean
    public ExecutorService threadPoolTaskExecutor()  {
        ThreadFactory factory = new CustomerThreadFactory(  inboundCallbackProperties.getNamePrefix()  );
        return new ThreadPoolExecutor(
                this.inboundCallbackProperties.getCorePoolSize(), this.inboundCallbackProperties.getMaxPoolSize(),
                this.inboundCallbackProperties.getKeepAliveSeconds(), TimeUnit.SECONDS,
                createQueue(this.inboundCallbackProperties.getQueueCapacity()), factory,  new ThreadPoolExecutor.AbortPolicy());
    }

    private BlockingQueue<Runnable> createQueue(int queueCapacity) {
        if (queueCapacity > 0) {
            return new LinkedBlockingQueue<>(queueCapacity);
        } else {
            return new SynchronousQueue<>();
        }
    }
}
