package cc.efit.process.biz.handler.flow;

import cc.efit.dialogue.api.vo.global.TemplateGlobalDefaultVerbalInfo;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.process.biz.handler.utils.TemplateGlobalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.efit.process.api.constants.DialogueRedisConstant.TEMPLATE_GLOBAL_DEFAULT_INFO;

@Slf4j
@Service("globalDefaultVerbalHandler")
public class GlobalDefaultVerbalHandler extends AbstractFlowHandler {
    @Override
    public List<BaseActionData> handler(DialogueProcessSession session, ChatProcessReq req) {
        log.info("callId:{}无任何匹配全局兜底话术",req.callId());
        Integer callTemplateId = req.callTemplateId();
        String defaultVerbalKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_GLOBAL_DEFAULT_INFO, callTemplateId);
        TemplateGlobalDefaultVerbalInfo info = getRedisObject(defaultVerbalKey, TemplateGlobalDefaultVerbalInfo.class);
        if (info==null || !info.enabled()) {
            log.info("callId:{},global default verbal not enable for callTemplateId:{}", req.callId(),callTemplateId);
            return null;
        }
        return TemplateGlobalUtils.buildTemplateDefaultVerbalActionData(session,info,req.customerInfo());
    }
}
