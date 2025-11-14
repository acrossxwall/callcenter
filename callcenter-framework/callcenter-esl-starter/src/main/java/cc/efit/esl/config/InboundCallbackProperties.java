package cc.efit.esl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "inbound.callback")
@Data
public class InboundCallbackProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
    /**
     * 工作线程 超过maxPoolSize 时继续添加直接拒绝抛出异常
     * 最多10个线程来处理任务，其他都去排队
     */
    private int maxPoolSize = 10;

    /**
     * queue size 为0 意味着当 corePoolSize 值达到时，且工作线程小于maxPoolSize
     * 时，后续线程直接执行
     */
    private int queueCapacity = 0;
    /**
     * 线程空闲超时时间 单位s
     */
    private int keepAliveSeconds = 60;
    /**
     * 线程前缀
     */
    private String namePrefix = "esl-callback";

}
