package cc.efit.dial.biz.mq;

import cc.efit.dial.api.constant.DialKeyConstants;
import cc.efit.dial.biz.service.DialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DialPhoneListener {
    @Autowired
    private DialService dialService;

    @RabbitListener( concurrency="20",
            bindings = @QueueBinding(
                    exchange = @Exchange( name = DialKeyConstants.DIAL_PHONE_MQ_EXCHANGE_NAME),
                    value = @Queue(DialKeyConstants.DIAL_PHONE_MQ_QUEUE),
                    key = DialKeyConstants.DIAL_PHONE_MQ_QUEUE
            ))
    public void handlerDialPhone(String msg) {
        log.info("接收到拨打电话消息:{}",msg);
        dialService.dialPhone(msg);
    }
}
