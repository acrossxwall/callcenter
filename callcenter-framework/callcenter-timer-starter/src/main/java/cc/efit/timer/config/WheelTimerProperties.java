package cc.efit.timer.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "timer.wheel")
public class WheelTimerProperties {
    /**
     * ticksPerWheel
     *  核心定时器：tickDuration=100ms, wheel 大小512 → 可覆盖51.2秒，建议根据业务调整
     */
    private int ticksPerWheel = 512;
    private  long tickDuration = 100;
    private int maxPendingTimeouts = 1000000;
    private String poolName = "timer-pool";
}
