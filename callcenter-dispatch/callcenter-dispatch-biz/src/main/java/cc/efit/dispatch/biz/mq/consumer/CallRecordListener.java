package cc.efit.dispatch.biz.mq.consumer;

import cc.efit.dial.api.constant.DialKeyConstants;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dispatch.api.constant.CallRecordConstant;
import cc.efit.dispatch.biz.service.CallRecordService;
import cc.efit.dispatch.biz.service.CallTaskService;
import cc.efit.json.utils.JsonUtils;
import cc.efit.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CallRecordListener {
    @Autowired
    private CallRecordService callRecordService;
    @Autowired
    private CallTaskService callTaskService;
    @Autowired
    private RedisUtils redisUtils;

    @RabbitListener( concurrency="20",
            bindings = @QueueBinding(
                    exchange = @Exchange( name = DialKeyConstants.DIAL__CALL_RECORD_MQ_EXCHANGE_NAME),
                    value = @Queue(DialKeyConstants.DIAL_CALL_RECORD_QUEUE_KEY),
                    key = DialKeyConstants.DIAL_CALL_RECORD_QUEUE_KEY
            ))
    public void handlerCallRecord(String callRecord) {
        log.info("收到通话记录mq:{}",callRecord);
        DialProcessSession session = JsonUtils.parseObject(callRecord, DialProcessSession.class);
        String callId = session.getCallId();
        try {
            boolean handler = redisUtils.sHasKey(CallRecordConstant.CALL_RECORD_SET_KEY, callId);
            if (handler) {
                //简单处理下，可能还会有原子问题，通话记录已经处理过 如果使用redisson加锁应该可以解决
                log.info("通话记录已经处理过:{}",callId);
                return;
            }
            redisUtils.sSet(CallRecordConstant.CALL_RECORD_SET_KEY, callId);
            releaseTaskConcurrent(session.getTaskId(), session.isReleaseSemaphore());

            log.info("处理通话记录mq:{}",callId);
            callRecordService.handlerCallRecord(session);
            session.setReleaseSemaphore(false);
        } catch (Exception e){
            //应该再次消费，防止丢失数据，或者再次发送mq
            log.error("处理通话记录mq异常",e);
            redisUtils.setRemove(CallRecordConstant.CALL_RECORD_SET_KEY, callId);
            throw new RuntimeException(e);
        }

    }

    private void releaseTaskConcurrent(Integer taskId, boolean releaseSemaphore) {
        if (releaseSemaphore){
            log.info("释放任务调度并发数：{}",taskId);
            callTaskService.releaseTaskDispatchConcurrencySemaphore(taskId);
        }
    }
}
