package cc.efit.data.config;

import cc.efit.data.permission.DataPermissionIgnoreAspect;
import cc.efit.data.permission.DataPermissionInterceptor;
import cc.efit.data.permission.DataPermissionProperties;
import cc.efit.org.config.OrgPermissionAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = OrgPermissionAutoConfiguration.class)
@EnableConfigurationProperties(DataPermissionProperties.class)
@ConditionalOnProperty(prefix = "data", name = "enable", havingValue = "true")
public class DataPermissionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DataPermissionInterceptor.class)
    public DataPermissionInterceptor dataPermissionInterceptor(DataPermissionProperties dataPermissionProperties) {
        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor();
        dataPermissionInterceptor.setPermissionProperties(dataPermissionProperties);
        return dataPermissionInterceptor;
    }
    @Bean
    @ConditionalOnMissingBean(DataPermissionIgnoreAspect.class)
    public DataPermissionIgnoreAspect dataPermissionIgnoreAspect() {
        return new DataPermissionIgnoreAspect();
    }
}
