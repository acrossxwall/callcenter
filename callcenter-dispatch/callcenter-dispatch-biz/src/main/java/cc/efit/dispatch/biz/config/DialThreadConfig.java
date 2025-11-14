package cc.efit.dispatch.biz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class DialThreadConfig {

    @Bean("dialExecutors")
    public ExecutorService getDialExecutors() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
