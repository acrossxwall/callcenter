package cc.efit.process.biz.handler.flow;

import cc.efit.dialogue.api.enums.TemplateFlowTypeEnum;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.constants.DialogueRedisConstant;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 跳转流程处理器 和 PredictFlowHandler 逻辑基本一致为了判断当前需要跳转 或者匹配到哪个节点的
 */
@Service("jumpFlowHandler")
@Slf4j
public class JumpFlowHandler extends AbstractFlowHandler{
    @Override
    public List<BaseActionData> handler(DialogueProcessSession session, ChatProcessReq req) {
        Integer flowId = session.getFlowId();
        Integer nodeType = session.getNodeType();
        log.info("callId:{},current flow id:{}, flow node type:{}",req.callId(),flowId,nodeType);
        if (TemplateFlowTypeEnum.JUMP.getCode().equals(nodeType)) {
            log.info("enter jump flow handler:{}",req.callId());
            //进入到跳转节点匹配
            String flowNodeKey = FormatUtil.formatTemplateRedisKey(DialogueRedisConstant.TEMPLATE_FLOW_NODE_INFO,session.getCallTemplateId(), flowId);
            TemplateNodeInfo flowNode = getRedisObject(flowNodeKey, TemplateNodeInfo.class);
            log.info("callId:{},进入跳转节点处理",req.callId());
            //直接跳转到指定的普通节点，不匹配知识库，如果target flow id为空则表明话术模板配置错误
            //直接走全局兜底的话术，故不在判断为空
            session.setFlowId(flowNode.getTargetFlowId());
            session.setMatchKnowledge(false);
            session.getMatchNodeLabels().add(flowNode.getNodeLabel());
        }
        return null;
    }
}
