package cc.efit.dispatch.api.constant;

public class DispatchRedisKeyConstant {

    /**
     * 任务信息缓存
     */
    public static final String DISPATCH_TASK_INFO_KEY = "dispatch:task:%s:info";

    /**
     * 任务分配并发信息缓存
     */
    public static final String DISPATCH_TASK_ASSIGN_INFO_KEY = "dispatch:task:%s:assign";

    /**
     * 启动任务锁
     */
    public static final String DISPATCH_TASK_LOCK_KEY = "dispatch:task:%s:lock";

    /**
     * 暂停任务锁
     */
    public static final String DISPATCH_TASK_PAUSE_LOCK_KEY = "dispatch:task:%s:pause:lock";
    /**
     * 加载任务数据锁
     */
    public static final String DISPATCH_TASK_LOAD_DATA_LOCK_KEY = "dispatch:task:load:data:%s:lock";
    /**
     * 任务并发数
     */
    public static final String DISPATCH_TASK_CONCURRENCY = "dispatch:task:%s:concurrency";
    /**
     * 待拨打数据redis key
     */
    public static final String DISPATCH_TASK_FIRST_CUSTOMER = "dispatch:task:first:%s:customer";
    /**
     * 已拨打数据redis key
     */
    public static final String DISPATCH_TASK_ALREADY_CALL = "dispatch:task:already:call:%s";
    /**
     * 重呼待拨打数据redis key
     */
    public static final String DISPATCH_TASK_REPEAT_CUSTOMER = "dispatch:task:repeat:%s:customer";
    /**
     * 待拨打数据锁
     */
    public static final String DISPATCH_TASK_CUSTOMER_LOCK = "dispatch:task:%s:customer:lock";
    /**
     * 黑名单信息
     */
    public static final String DISPATCH_BLACK_INFO  = "black:%s:phone:%s";
    /**
     * 调整并发锁  dispatch:adjust:concurrent:lock:{lineId}:{deptId}
     */
    public static final String DISPATCH_ADJUST_CONCURRENT_LOCK = "dispatch:adjust:concurrent:lock:%s:%s";
    /**
     * 调整并发信息
     */
    public static final String DISPATCH_ADJUST_CONCURRENT_INFO = "dispatch:adjust:concurrent:info:%s:%s";

    /**
     * 上次线路的分配并发
     */
    public static final String DISPATCH_ADJUST_CONCURRENT_LAST = "dispatch:adjust:concurrent:last:%s:%s";

    /**
     *  线路的分配并发
     */
    public static final String DISPATCH_ADJUST_CONCURRENT = "dispatch:adjust:concurrent:%s:%s";
    /**
     * 调整线路 的缓存信息
     */
    public static final String DISPATCH_ADJUST_LINE_REDUCE = "dispatch:adjust:line:reduce:%s:%s";
    /**
     * 降低线路回收并发锁
     */
    public static final String DISPATCH_ADJUST_LINE_REDUCE_TASK_LOCK = "dispatch:adjust:line:reduce:task:%s:%s:lock";

    /**
     * 降低线路回收并发的信号量
     */
    public static final String DISPATCH_ADJUST_LINE_REDUCE_TASK_SEMAPHORE  = "dispatch:adjust:line:reduce:task:%s:%s:semaphore";

}
