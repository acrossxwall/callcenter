package cc.efit.nlu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nlu.model")
@Data
public class ModelProperties {

    private String url;
}
