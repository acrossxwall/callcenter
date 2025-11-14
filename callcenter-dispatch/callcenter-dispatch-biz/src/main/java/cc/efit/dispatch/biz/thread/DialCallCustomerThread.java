package cc.efit.dispatch.biz.thread;

import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.dispatch.biz.service.CallTaskService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DialCallCustomerThread implements Runnable {

    private final Integer taskId;
    private final CallTaskService callTaskService;
    public DialCallCustomerThread(Integer taskId) {
        this.taskId = taskId;
        this.callTaskService = SpringBeanHolder.getBean(CallTaskService.class);
    }

    @Override
    public void run() {
        log.info("开始调度任务:{}",taskId);
        callTaskService.dispatchCallCustomer(taskId);
        log.info("调度任务:{}结束",taskId);
    }
}
