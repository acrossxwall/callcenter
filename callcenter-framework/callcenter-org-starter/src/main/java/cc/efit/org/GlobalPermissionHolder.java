package cc.efit.org;

import com.alibaba.ttl.TransmittableThreadLocal;

public class GlobalPermissionHolder {

    private static ThreadLocal<Boolean> ignoreThreadLocal = new TransmittableThreadLocal<>();

    public static void clear() {
        ignoreThreadLocal.remove();
    }

    public static boolean isIgnore() {
        return ignoreThreadLocal.get()!=null && ignoreThreadLocal.get();
    }

    public static void setIgnore(boolean ignore) {
        ignoreThreadLocal.set(ignore);
    }
}
