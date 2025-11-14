package cc.efit.call.api.constants;

public class DispatchKeyConstants {
    /**
     * 任务调度拨打key
     */
    public static final String DISPATCH_CALL_START_KEY = "dispatch:call:start";
    /**
     * 任务取消拨打key
     */
    public static final String DISPATCH_CALL_PAUSE_KEY = "dispatch:call:pause";
    /**
     * 任务加载待拨打数据key
     */
    public static final String DISPATCH_REDIS_LOAD_DATA = "dispatch:call:load:data";
    /**
     * 任务调度拨打
     */
    public static final String DISPATCH_CALL_TASK_MQ_QUEUE_KEY = "dispatch:call:task:queue";

    /**
     * 任务调度取消
     */
    public static final String DISPATCH_CALL_TASK_PAUSE_MQ_QUEUE_KEY = "dispatch:call:task:pause:queue";
    public static final String DISPATCH_CALL_TASK_LOAD_DATA_MQ_QUEUE_KEY = "dispatch:call:task:load:data:queue";
    public static final String DISPATCH_CALL_TASK_MQ_EXCHANGE = "dispatch:call:task:exchange";

    /**
     *  线路信息 线路不和任务绑定了
     */
    public static final String DISPATCH_LINE_KEY = "dispatch:line:%s";
    /**
     * 调度任务的ben名称
     */
    public static final String DISPATCH_CALL_TASK_BEAN_HANDLE = "dispatchCallTaskHandler";
}
