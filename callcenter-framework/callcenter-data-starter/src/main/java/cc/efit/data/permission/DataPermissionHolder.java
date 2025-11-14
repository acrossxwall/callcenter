package cc.efit.data.permission;

import com.alibaba.ttl.TransmittableThreadLocal;

public class DataPermissionHolder {


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
