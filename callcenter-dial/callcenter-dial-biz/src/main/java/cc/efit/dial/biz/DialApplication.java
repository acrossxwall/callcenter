package cc.efit.dial.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cc.efit"})
public class DialApplication {
    public static void main(String[] args) {
        SpringApplication.run(DialApplication.class, args);
    }
}
