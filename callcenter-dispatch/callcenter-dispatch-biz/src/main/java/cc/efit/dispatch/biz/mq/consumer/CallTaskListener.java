package cc.efit.dispatch.biz.mq.consumer;

import cc.efit.call.api.constants.DispatchKeyConstants;
import cc.efit.dispatch.biz.service.CallTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CallTaskListener {
    @Autowired
    private CallTaskService callTaskService;

    @RabbitListener( concurrency="2",
            bindings = @QueueBinding(
                    exchange = @Exchange( name = DispatchKeyConstants.DISPATCH_CALL_TASK_MQ_EXCHANGE),
                    value = @Queue(DispatchKeyConstants.DISPATCH_CALL_TASK_MQ_QUEUE_KEY),
                    key = DispatchKeyConstants.DISPATCH_CALL_TASK_MQ_QUEUE_KEY
            ))
    public void handlerCallTaskDispatch(String taskId) {
        log.info("接收到拨打任务：{}", taskId);
        try {
            callTaskService.dispatchCallTask(Integer.valueOf( taskId));
        }catch (Exception e){
            //TODO 发送提醒或者 再次处理
            log.error("处理拨打任务异常", e);
        }
    }

    @RabbitListener( concurrency="2",
            bindings = @QueueBinding(
                    exchange = @Exchange( name = DispatchKeyConstants.DISPATCH_CALL_TASK_MQ_EXCHANGE),
                    value = @Queue(DispatchKeyConstants.DISPATCH_CALL_TASK_PAUSE_MQ_QUEUE_KEY),
                    key = DispatchKeyConstants.DISPATCH_CALL_TASK_PAUSE_MQ_QUEUE_KEY
            ))
    public void handlerCallTaskPause(String taskId) {
        log.info("接收到拨打任务取消：{}", taskId);
        try {
            callTaskService.pauseDispatchCallTask(Integer.valueOf( taskId));
        }catch (Exception e){
            //TODO 发送提醒或者 再次处理
            log.error("处理取消拨打任务异常", e);
        }
    }

    @RabbitListener( concurrency="2",
            bindings = @QueueBinding(
                    exchange = @Exchange( name = DispatchKeyConstants.DISPATCH_CALL_TASK_MQ_EXCHANGE),
                    value = @Queue(DispatchKeyConstants.DISPATCH_CALL_TASK_LOAD_DATA_MQ_QUEUE_KEY),
                    key = DispatchKeyConstants.DISPATCH_CALL_TASK_LOAD_DATA_MQ_QUEUE_KEY
            ))
    public void handlerCallTaskLoadData(String taskId) {
        log.info("接收到拨打任务加载数据的线程：{}", taskId);
        try {
            callTaskService.dispatchCallTaskLoadData(Integer.valueOf( taskId));
        }catch (Exception e){
            //TODO 发送提醒或者 再次处理
            log.error("处理拨打任务异常", e);
        }
    }
}
