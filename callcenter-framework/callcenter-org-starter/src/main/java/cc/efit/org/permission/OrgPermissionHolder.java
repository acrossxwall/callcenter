package cc.efit.org.permission;

import com.alibaba.ttl.TransmittableThreadLocal;

public class OrgPermissionHolder {

    private static ThreadLocal<Integer> orgIdThreadLocal = new TransmittableThreadLocal<>();

    private static ThreadLocal<Boolean> ignoreThreadLocal = new TransmittableThreadLocal<>();

    public static Integer getCurrentOrgId() {
        return orgIdThreadLocal.get();
    }

    public static void setCurrentOrgId(Integer orgId) {
        orgIdThreadLocal.set(orgId);
    }

    public static void clear() {
        orgIdThreadLocal.remove();
        ignoreThreadLocal.remove();
    }

    public static boolean isIgnore() {
        return ignoreThreadLocal.get()!=null && ignoreThreadLocal.get();
    }

    public static void setIgnore(boolean ignore) {
        ignoreThreadLocal.set(ignore);
    }
}
