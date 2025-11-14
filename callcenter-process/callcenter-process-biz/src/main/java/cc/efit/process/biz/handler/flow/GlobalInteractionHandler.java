package cc.efit.process.biz.handler.flow;

import cc.efit.utils.DateUtil;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.dialogue.api.vo.global.TemplateGlobalInteractionInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.handler.utils.TemplateGlobalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static cc.efit.process.api.constants.DialogueRedisConstant.TEMPLATE_GLOBAL_INTERACTION;

@Service("globalInteractionHandler")
@Slf4j
@RequiredArgsConstructor
public class GlobalInteractionHandler extends AbstractFlowHandler {

    @Override
    public List<BaseActionData> handler(DialogueProcessSession session, ChatProcessReq req) {
        log.info("enter global interaction handler:{}", req.callId());
        Integer callTemplateId = req.callTemplateId();
        String interactionType = FormatUtil.format(TEMPLATE_GLOBAL_INTERACTION, callTemplateId);
        TemplateGlobalInteractionInfo info = getRedisObject(interactionType, TemplateGlobalInteractionInfo.class);
        if (info==null || !info.enabled()) {
            log.info("callId:{},global interaction template not enable for callTemplateId:{}", req.callId(),callTemplateId);
            return null;
        }
        long duration = DateUtil.diffSeconds(session.getAnswerTime(), LocalDateTime.now());
        log.info("callId:{} , interactive count:{}, duration:{}", req.callId(),session.getInteractiveCount(), duration);
        boolean bingo = session.getInteractiveCount() >= info.maxInteractiveCount() ||  duration  >= (info.maxDuration() * 60);
        if (bingo) {
            log.info("callId:{},global interaction template bingo for callTemplateId:{}", req.callId(),callTemplateId);
            //命中全局交互设置轮次或者最大通话时长
            return TemplateGlobalUtils.buildTemplateGlobalActionData(session,info,req.customerInfo());
        }
        return null;
    }
}
