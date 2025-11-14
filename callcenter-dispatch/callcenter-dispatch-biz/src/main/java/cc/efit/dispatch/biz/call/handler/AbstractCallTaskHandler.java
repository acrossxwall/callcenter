package cc.efit.dispatch.biz.call.handler;

import cc.efit.call.api.domain.CallTask;
import cc.efit.call.api.enums.CallCustomerStatusEnum;
import cc.efit.call.api.repository.CallCustomerRepository;
import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.dial.api.constant.DialKeyConstants;
import cc.efit.dial.api.req.DialPhoneReq;
import cc.efit.dispatch.api.constant.DispatchRedisKeyConstant;
import cc.efit.dispatch.biz.call.CallTaskHandler;
import cc.efit.dispatch.biz.mq.producer.MessageProducer;
import cc.efit.dispatch.biz.risk.RiskManagerHandlerChain;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisUtils;
import org.redisson.api.RSemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCallTaskHandler implements CallTaskHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected abstract String waitCallCustomerKey(Integer taskId) ;

    protected abstract boolean needUpdateCustomerStatus();
    @Override
    public void handleCallTask(CallTask task, RSemaphore semaphore, int availablePermits) {
        log.info("处理呼叫任务:{}", task.getId());
        String waitCallKey = waitCallCustomerKey(task.getId());
        RedisUtils redisUtils = SpringBeanHolder.getBean(RedisUtils.class);
        RiskManagerHandlerChain riskManagerHandlerChain = SpringBeanHolder.getBean(RiskManagerHandlerChain.class);
        MessageProducer producer = SpringBeanHolder.getBean(MessageProducer.class);
        CallCustomerRepository customerRepository = SpringBeanHolder.getBean(CallCustomerRepository.class);
        List<Integer> ids = new ArrayList<>();
        for (int i=0;i<availablePermits;i++) {
            boolean acquire = semaphore.tryAcquire();
            if (!acquire) {
                break;
            }
            try {
                Object result = redisUtils.leftPop(waitCallKey);
                if (result==null) {
                    semaphore.release();
                    break;
                }
                DialPhoneReq req = JsonUtils.parseObject(result.toString(), DialPhoneReq.class);
                String callId = req.customer().callId();
                if (checkTaskAlreadyCall(task.getId(), callId, redisUtils)) {
                    log.info("呼叫任务已呼叫:{},callId:{}", task.getId(),callId);
                    semaphore.release();
                    continue;
                }
                //校验黑名单或者其他拦截规则，链式调用
                boolean risk = riskManagerHandlerChain.riskManagerChain(req.customer(), task);
                if (risk) {
                    log.info("拦截呼叫任务:{},callId:{}", task.getId(),callId);
                    semaphore.release();
                    continue;
                }
                //发送mq进行呼叫
                producer.send(DialKeyConstants.DIAL_PHONE_MQ_QUEUE, result);
                if (needUpdateCustomerStatus()){
                    ids.add(req.customer().id());
                    if (ids.size()== 500) {
                        customerRepository.updateStatusByIds(ids, CallCustomerStatusEnum.CustomerStatus.CALLING.getStatus());
                        ids.clear();
                    }
                }
            }catch (Exception e) {
                log.error("呼叫任务异常", e);
                semaphore.release();
            }
        }
        if (needUpdateCustomerStatus() && !ids.isEmpty()) {
            customerRepository.updateStatusByIds(ids, CallCustomerStatusEnum.CustomerStatus.CALLING.getStatus());
        }
    }

    /**
     * 幂等校验call id 是否拨打过，防止重复拨打
     * 添加redis set 集合，添加成功，没有拨打过 返回false ，添加失败，已经拨打过 返回true
     * @param taskId        任务id
     * @param callId         呼叫id
     * @return true:已经拨打过，false:未拨打过
     */
    private boolean checkTaskAlreadyCall(Integer taskId, String callId, RedisUtils redisUtils) {
        String key = DispatchRedisKeyConstant.DISPATCH_TASK_ALREADY_CALL.formatted(taskId);
        return redisUtils.sSet(key, callId) == 0;
    }
}
