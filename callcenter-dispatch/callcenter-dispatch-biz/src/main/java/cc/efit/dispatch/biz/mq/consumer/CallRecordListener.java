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
        if (session == null) {
            log.warn("通话记录反序列化为空，跳过");
            return;
        }
        String callId = session.getCallId();
        if (callId == null || callId.isEmpty()) {
            log.warn("通话记录 callId 为空，跳过");
            return;
        }
        try {
            // SADD 返回新增成员数：并发消费下仅首个实例返回 1，其余返回 0，避免「先判是否存在再写入」的竞态
            long added = redisUtils.sSet(CallRecordConstant.CALL_RECORD_SET_KEY, callId);
            if (added == 0L) {
                log.info("通话记录已处理或正在被处理，跳过:{}", callId);
                return;
            }
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
