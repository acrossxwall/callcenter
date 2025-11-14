package cc.efit.dispatch.biz.thread;

import cc.efit.call.api.constants.DispatchKeyConstants;
import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.dispatch.biz.service.CallTaskService;
import cc.efit.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadWaitCallCustomerThread implements Runnable {
    private final Integer taskId;
    private final CallTaskService callTaskService;
    private final RedisUtils redisUtils;
    private boolean start;
    public LoadWaitCallCustomerThread(Integer taskId){
        this.taskId=taskId;
        this.callTaskService = SpringBeanHolder.getBean(CallTaskService.class);
        this.redisUtils = SpringBeanHolder.getBean(RedisUtils.class);
        this.start = true;
    }
    @Override
    public void run() {
        log.info("开始加载待呼叫客户，任务id:{}",taskId);
        while(redisUtils.sHasKey(DispatchKeyConstants.DISPATCH_CALL_START_KEY, taskId)) {
            boolean loadFinish = callTaskService.loadWaitCallCustomer(taskId,start);
            start = false;
            if (loadFinish) {
                //说明当前加载完毕，可以释放调加载数据线程，但是后续可能还是会传输任务
                //故需要在传入号码启动任务的时候再次判断是否包含加载线程
                redisUtils.setRemove(DispatchKeyConstants.DISPATCH_REDIS_LOAD_DATA,taskId);
                break;
            }
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                //ignore
            }
        }
        log.info("call task:{},加载待呼叫客户完成",taskId);
    }
}
