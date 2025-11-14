package cc.efit.job.config;

import cc.efit.job.core.AdminProperties;
import cc.efit.job.core.ExecutorProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xxl.job")
@Data
public class XxlJobProperties {

    private AdminProperties admin;

    private ExecutorProperties executor;
}
