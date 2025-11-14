package cc.efit.dispatch.biz.config;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("dialExecutors")
@Documented
public @interface DialDataExecutor {
}
