package cc.efit.dialogue.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "template")
@Data
public class TemplateConfig {
    /**
     * 上传话术录音配置  默认值 template/{orgId}/{callTemplateId}/verbal/
     */
    private String verbalPath = "template/%s/%s/verbal/";
    /**
     * 上传话术语料配置  默认值 template/{orgId}/{callTemplateId}/corpus/
     */
    private String corpusPath = "template/%s/%s/corpus/";
}
