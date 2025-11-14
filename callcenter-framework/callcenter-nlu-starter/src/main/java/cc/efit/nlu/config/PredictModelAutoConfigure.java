package cc.efit.nlu.config;

import cc.efit.nlu.INluModelService;
import cc.efit.nlu.core.NluModelServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

@AutoConfiguration
@EnableConfigurationProperties(ModelProperties.class)
public class PredictModelAutoConfigure {

    @Bean
    public INluModelService nluModelService(ModelProperties modelProperties){
        if (!StringUtils.hasLength(modelProperties.getUrl())) {
            throw  new RuntimeException("nlu model url is empty");
        }
        return new NluModelServiceImpl(modelProperties);
    }
}
