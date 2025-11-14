package cc.efit.org.config;

import cc.efit.org.permission.OrgIgnoreAspect;
import cc.efit.org.permission.OrgPermissionInterceptor;
import cc.efit.org.permission.OrgPermissionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author across
 * @Description
 * @Date 2025-08-22 20:26
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "org", name = "enable", havingValue = "true")
@EnableConfigurationProperties(OrgPermissionProperties.class)
public class OrgPermissionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(OrgPermissionInterceptor.class)
    public OrgPermissionInterceptor orgPermissionInterceptor(OrgPermissionProperties orgPermissionProperties) {
        OrgPermissionInterceptor orgPermissionInterceptor = new OrgPermissionInterceptor();
        orgPermissionInterceptor.setPermissionProperties(orgPermissionProperties);
        return orgPermissionInterceptor;
    }
    @Bean
    @ConditionalOnMissingBean(OrgIgnoreAspect.class)
    public OrgIgnoreAspect orgIgnoreAspect() {
        return new OrgIgnoreAspect();
    }
}
