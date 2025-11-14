package cc.efit.call.biz.config;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("importDataPoolTaskExecutor")
@Documented
public @interface ImportDataExecutor {
}
