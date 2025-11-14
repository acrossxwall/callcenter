package cc.efit.dial.biz.service.impl;

import cc.efit.dial.api.constant.DialKeyConstants;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dial.api.req.DialPhoneReq;
import cc.efit.dial.api.req.LineInfoReq;
import cc.efit.dial.biz.client.FsApiCommand;
import cc.efit.dial.biz.service.DialService;
import cc.efit.json.utils.JsonUtils;
import cc.efit.process.api.res.TemplateGlobalSettingRes;
import cc.efit.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class DialServiceImpl implements DialService {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private FsApiCommand fsApiCommand;
    @Override
    public void dialPhone(String msg) {
        //构建新的拨打电话session
        DialPhoneReq dialPhoneReq = JsonUtils.parseObject(msg,DialPhoneReq.class);
        dialPhone(dialPhoneReq);
    }

    @Override
    public void dialPhone(DialPhoneReq req) {
        LineInfoReq lineInfo = req.lineInfo();
        CallCustomerInfoReq customer = req.customer();
        String callId = customer.callId();
        Integer taskId = customer.taskId();
        log.info("任务开始拨打电话:{},{}",taskId,callId);
        createDialogueProcessSession(callId,lineInfo,customer,req.settings());
        fsApiCommand.callPhone(lineInfo, customer);
    }

    @Override
    public DialProcessSession createDialogueProcessSession(String callId, LineInfoReq lineInfo, CallCustomerInfoReq customer,
                                                           TemplateGlobalSettingRes settings) {
        DialProcessSession session = new DialProcessSession();
        session.setCallId(callId);
        session.setLineInfo(lineInfo);
        session.setCustomer(customer);
        session.setCreateTime(LocalDateTime.now());
        session.setStatus(ProcessSessionStatusEnum.DIALED);
        if (settings!=null) {
            session.setInterruptSeconds(settings.interruptSeconds());
            session.setNoReplySeconds(settings.noReplySeconds());
            session.setEnableInterrupt(settings.enableInterrupt());
            session.setEnableNoReply(settings.enableNoReply());
        }
        redisUtils.set(DialKeyConstants.DIAL_SESSION_KEY.formatted(callId),JsonUtils.toJsonString(session));
        return session;
    }
}
