package cc.efit.data.permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DataPermissionIgnoreAspect {

    @Around("@annotation(dataPermissionIgnore)")
    public Object around(ProceedingJoinPoint joinPoint, DataPermissionIgnore dataPermissionIgnore) throws Throwable {
        boolean oldIgnore = DataPermissionHolder.isIgnore();
        try {
            DataPermissionHolder.setIgnore(true);
            // 执行逻辑
            return joinPoint.proceed();
        } finally {
            DataPermissionHolder.setIgnore(oldIgnore);
        }
    }

}