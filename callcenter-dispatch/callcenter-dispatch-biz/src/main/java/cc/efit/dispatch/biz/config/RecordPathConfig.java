package cc.efit.dispatch.biz.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "record.path")
@Configuration
@Getter
public class RecordPathConfig {
    /**
     * fs 录音的地址 /home/recordings/{yyyy-MM-dd}/{uuid}.wav
     */
    private String fs = "/home/recordings/%s/%s.wav";
    /**
     * db 录音的地址 默认值 records/{orgId}/{yyyy-MM-dd}/{callId}.wav
     */
    private String db = "records/%s/%s/%s.wav";

}
