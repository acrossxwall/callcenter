package cc.efit.dispatch.biz.call.handler;

import cc.efit.dispatch.api.constant.DispatchRedisKeyConstant;
import cc.efit.dispatch.api.enums.DispatchCallTaskTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RepeatCallTaskHandler extends AbstractCallTaskHandler {

    @Override
    public DispatchCallTaskTypeEnum getDispatchCallTaskTypeEnum() {
        return DispatchCallTaskTypeEnum.REPEAT_CALL;
    }

    @Override
    protected String waitCallCustomerKey(Integer taskId) {
        return DispatchRedisKeyConstant.DISPATCH_TASK_REPEAT_CUSTOMER.formatted(taskId);
    }

    @Override
    protected boolean needUpdateCustomerStatus() {
        return false;
    }
}
