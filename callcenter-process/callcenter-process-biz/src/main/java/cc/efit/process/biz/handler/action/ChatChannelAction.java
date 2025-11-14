package cc.efit.process.biz.handler.action;

import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.enums.flow.TemplateFlowStatusEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.biz.handler.BaseFlowHandlerChain;
import cc.efit.process.api.core.DialogueProcessSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("chatChannelAction")
@Slf4j
@RequiredArgsConstructor
public class ChatChannelAction  extends AbstractActionHandler {

    private final BaseFlowHandlerChain flowHandlerChain;
    @Override
    public List<BaseActionData> actionHandle(DialogueProcessSession session, ChatProcessReq req) {
        log.info("开始处理聊天请求,call id:{},template id:{},内容:{}",req.callId(),req.callTemplateId(),req.content());
        //默认先进行全局设置匹配-> 通用节点关键词或nlu匹配 -> 知识库匹配链式匹配
        //通用节点，包含两种 普通节点 和 跳转节点
        if (session.getStatus()!= TemplateFlowStatusEnum.PROCESSING) {
            log.info("当前会话callId:{},状态为:{},不是会话中",req.callId(),session.getStatus());
            return null;
        }
        //交互轮次+1
        session.setInteractiveCount(session.getInteractiveCount()+1);
        if (StringUtils.isBlank(req.content())) {
            //内容为空 代表未识别 无应答
            session.setNoReplyCount(session.getNoReplyCount()+1);
            session.setSilentCount(session.getSilentCount()+1);
        }
        return flowHandlerChain.flowHandleChain(session, req);
    }

    @Override
    public Integer getActionIndex() {
        return ProcessReqActionEnum.CHAT.getCode();
    }
}
