package cc.efit.org.config;

import cc.efit.org.CompositeStatementInspector;
import cc.efit.org.CustomerPermissionInspector;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class HibernateConfig {
    // 注入所有实现了 CustomerPermissionInspector 的 Bean（你的多个拦截器）
    @Autowired(required = false)
    private List<CustomerPermissionInspector> inspectorBeans;

    @Bean("compositeStatementInspector")
    public CompositeStatementInspector compositeInspector() {
        CompositeStatementInspector composite = new CompositeStatementInspector();
        // 将 Spring 容器中的所有拦截器添加到复合拦截器中
        if (inspectorBeans!=null) {
            inspectorBeans.forEach(composite::addInspector);
        }
        return composite;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return properties -> {
            // 直接设置 StatementInspector 实例
            properties.put(AvailableSettings.STATEMENT_INSPECTOR,  compositeInspector());
        };
    }
}
