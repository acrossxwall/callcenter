package cc.efit.process.biz.handler.flow;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.dialogue.api.vo.global.TemplateGlobalNoReplyInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.handler.utils.TemplateGlobalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.efit.process.api.constants.DialogueRedisConstant.TEMPLATE_GLOBAL_NO_REPLY;

@Service("globalNoReplyHandler")
@Slf4j
@RequiredArgsConstructor
public class GlobalNoReplyHandler extends AbstractFlowHandler {


    @Override
    public List<BaseActionData> handler(DialogueProcessSession session, ChatProcessReq req) {
        log.info("enter global no reply handler:{}", req.callId());
        Integer callTemplateId = req.callTemplateId();
        String noReplyKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_NO_REPLY, callTemplateId);
        TemplateGlobalNoReplyInfo info = getRedisObject(noReplyKey, TemplateGlobalNoReplyInfo.class);
        if (info==null || !info.enabled()) {
            log.info("callId:{},global no reply not enable for callTemplateId:{}", req.callId(),callTemplateId);
            return null;
        }
        if (session.getNoReplyCount()>=info.maxNoreplyCount()) {
            log.info("callId:{},global no reply count:{} over max count:{}", req.callId(),session.getNoReplyCount(),info.maxNoreplyCount());
            return TemplateGlobalUtils.buildTemplateNoReplyActionData(session,info,req.customerInfo());
        }
        return null;
    }
}
