package cc.efit.esl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "inbound.freeswitch")
@Data
public class FreeswitchProperties {

    private String host;
    private int port;
    private String password;
    private String eventSubscriptions;
    private int timeoutSeconds;
}
