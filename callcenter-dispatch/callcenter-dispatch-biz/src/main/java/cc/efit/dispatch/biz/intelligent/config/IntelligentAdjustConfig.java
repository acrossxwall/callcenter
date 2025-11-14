package cc.efit.dispatch.biz.intelligent.config;

import cc.efit.dispatch.api.enums.IntelligentAdjustStrategy;
import cc.efit.dispatch.biz.intelligent.AllocationStrategy;
import cc.efit.dispatch.biz.intelligent.strategy.AverageStrategy;
import cc.efit.dispatch.biz.intelligent.strategy.PriorityStrategy;
import cc.efit.dispatch.biz.intelligent.strategy.SettingStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "intelligent.adjust")
public class IntelligentAdjustConfig {
    /**
     * 智能调整策略
     */
    private IntelligentAdjustStrategy  strategy;
    /**
     * 最小并发数
     */
    private int minConcurrency = 10;

    @Bean
    public AllocationStrategy intelligentAllocationStrategy() {
        if (strategy==null) {
            strategy = IntelligentAdjustStrategy.SETTING;
        }
        return switch (strategy) {
            case AVERAGE -> new AverageStrategy();
            case SETTING -> new SettingStrategy();
            case PRIORITY -> new PriorityStrategy(minConcurrency);
        };
    }
}
