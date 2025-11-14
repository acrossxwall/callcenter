package cc.efit.timer.config;

import cc.efit.timer.core.HashedWheelTimerService;
import cc.efit.timer.core.TimerService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(WheelTimerProperties.class)
@ConditionalOnMissingBean(TimerService.class)
public class TimerAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    public TimerService buildTimerService(WheelTimerProperties timerProperties) {
        return new HashedWheelTimerService(timerProperties);
    }
}
