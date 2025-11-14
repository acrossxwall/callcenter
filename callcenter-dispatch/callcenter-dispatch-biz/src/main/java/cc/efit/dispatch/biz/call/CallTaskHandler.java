package cc.efit.dispatch.biz.call;

import cc.efit.call.api.domain.CallTask;
import cc.efit.dispatch.api.enums.DispatchCallTaskTypeEnum;
import org.redisson.api.RSemaphore;

public interface CallTaskHandler {

    void handleCallTask(CallTask task, RSemaphore semaphore, int availablePermits);

    DispatchCallTaskTypeEnum getDispatchCallTaskTypeEnum();
}
