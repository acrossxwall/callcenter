package cc.efit.dispatch.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "call.config")
@Data
public class CallConfig {
    /**
     * 加载待呼叫数据的系数
     */
    private Integer factor = 10;
    /**
     * 每次更新待呼叫批次数据量
     */
    private Integer batchSize = 1000;
}
