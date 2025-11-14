package cc.efit.job.config;

import cc.efit.job.IJobService;
import cc.efit.job.core.AdminProperties;
import cc.efit.job.core.ExecutorProperties;
import cc.efit.job.core.JobServiceImpl;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({XxlJobProperties.class})
public class XxlJobAutoConfiguration {

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties xxlJobProperties) {
        AdminProperties admin = xxlJobProperties.getAdmin();
        ExecutorProperties executor = xxlJobProperties.getExecutor();
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(admin.getAddresses());
        xxlJobSpringExecutor.setAccessToken(admin.getAccessToken());
        xxlJobSpringExecutor.setTimeout(admin.getTimeout());
        xxlJobSpringExecutor.setAppname(executor.getAppname());
        xxlJobSpringExecutor.setAddress(executor.getAddress());
        xxlJobSpringExecutor.setIp(executor.getIp());
        xxlJobSpringExecutor.setPort(executor.getPort());
        xxlJobSpringExecutor.setLogPath(executor.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

    @Bean
    public IJobService jobService(XxlJobProperties xxlJobProperties){
        return new JobServiceImpl(xxlJobProperties.getAdmin());
    }
}
