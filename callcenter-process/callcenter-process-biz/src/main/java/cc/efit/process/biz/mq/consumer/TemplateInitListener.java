package cc.efit.process.biz.mq.consumer;

import cc.efit.dialogue.api.constants.TemplateKeyConstants;
import cc.efit.dialogue.api.vo.template.TemplateInitInfo;
import cc.efit.json.utils.JsonUtils;
import cc.efit.process.api.ChatProcessVoipApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TemplateInitListener {
    @Autowired
    private ChatProcessVoipApi chatProcessVoipApi;

    @RabbitListener( concurrency="2",
            bindings = @QueueBinding(
                    exchange = @Exchange( name = TemplateKeyConstants.CALL_TEMPLATE_INIT_EXCHANGE),
                    value = @Queue(TemplateKeyConstants.CALL_TEMPLATE_INIT_QUEUE_KEY),
                    key = TemplateKeyConstants.CALL_TEMPLATE_INIT_QUEUE_KEY
            ))
    public void initTemplateToLocalCache(String msg) {
        log.info("接收到初始化模板消息：{}", msg);
        TemplateInitInfo templateInitInfo = JsonUtils.parseObject(msg, TemplateInitInfo.class);
        chatProcessVoipApi.initTemplateInfoToLocalCache(templateInitInfo.callTemplateId(), templateInitInfo.branchMap(),
                templateInitInfo.intentionMap(), templateInitInfo.knowledgeVoMap());
        log.info("初始化模板id:{},成功", templateInitInfo.callTemplateId());
    }
}
