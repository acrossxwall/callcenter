package cc.efit.org.permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Aspect
public class OrgIgnoreAspect {


    @Around("@annotation(orgPermissionIgnore)")
    public Object around(ProceedingJoinPoint joinPoint, OrgPermissionIgnore orgPermissionIgnore) throws Throwable {
        boolean oldIgnore = OrgPermissionHolder.isIgnore();
        try {
            OrgPermissionHolder.setIgnore(true);
            // 执行逻辑
            return joinPoint.proceed();
        } finally {
            OrgPermissionHolder.setIgnore(oldIgnore);
        }
    }

}