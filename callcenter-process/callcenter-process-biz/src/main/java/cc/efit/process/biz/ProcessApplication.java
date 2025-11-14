package cc.efit.process.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author across
 * @Description
 * @Date 2025-09-13 16:18
 */
@SpringBootApplication(scanBasePackages = {"cc.efit"})
public class ProcessApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessApplication.class, args);
    }
}
