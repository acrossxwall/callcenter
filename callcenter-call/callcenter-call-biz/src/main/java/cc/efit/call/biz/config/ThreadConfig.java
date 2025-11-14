package cc.efit.call.biz.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 使用虚拟线程配置，线程池的配置暂时保留
 */
@Configuration
@ConfigurationProperties("thread.pool")
@Getter
@Setter
public class ThreadConfig {
    /**
     * 核心线程数
     */
    private int corePoolSize = 10;
    /**
     * 工作线程 超过maxPoolSize 时继续添加直接拒绝抛出异常
     * 最多10个线程来处理任务，其他都去排队
     */
    private int maxPoolSize = 10;

    /**
     * queue size 为0 意味着当 corePoolSize 值达到时，且工作线程小于maxPoolSize
     * 时，后续线程直接执行
     */
    private int queueCapacity = 10000;
    /**
     * 线程空闲超时时间 单位s
     */
    private int keepAliveSeconds = 60;
    /**
     * 线程前缀
     */
    private String namePrefix = "import";

    @Bean(name = "importDataPoolTaskExecutor")
    public ExecutorService threadPoolTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }


//    @Bean(name = "importDataPoolTaskExecutor")
//    public ExecutorService threadPoolTaskExecutor()  {
//        ThreadFactory factory = new CustomerThreadFactory(  namePrefix  );
//        ThreadPoolExecutor executor  = new ThreadPoolExecutor(
//                this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS,
//                createQueue(this.queueCapacity), factory,  new ThreadPoolExecutor.AbortPolicy());
//        return TtlExecutors.getTtlExecutorService(executor);
//    }
//
//    private BlockingQueue<Runnable> createQueue(int queueCapacity) {
//        if (queueCapacity > 0) {
//            return new LinkedBlockingQueue<>(queueCapacity);
//        } else {
//            return new SynchronousQueue<>();
//        }
//	}
}
