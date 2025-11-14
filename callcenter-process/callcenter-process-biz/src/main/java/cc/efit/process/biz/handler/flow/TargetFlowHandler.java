package cc.efit.process.biz.handler.flow;

import cc.efit.process.api.ProcessServiceProto;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.constants.DialogueRedisConstant;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.handler.utils.TemplateFlowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("targetFlowHandler")
@Slf4j
public class TargetFlowHandler extends AbstractFlowHandler{
    @Override
    public List<BaseActionData> handler(DialogueProcessSession session, ChatProcessReq req) {
        log.info("enter target flow handler:{}",req.callId());
        //进入此处代表上一步已经匹配到 flow node 匹配知识库应该为false
        if ( session.isMatchKnowledge() ) {
            log.info("target flow handler match knowledge {}",req.callId());
            return null;
        }
        Integer targetFlowId  = session.getTargetFlowId();
        if ( targetFlowId == null ) {
            log.info("target flow handler flowId 配置错误{}",req.callId());
            return null;
        }
        log.info("当前call id:{},flow id:{} ",req.callId(), targetFlowId);
        String flowNodeKey = FormatUtil.formatTemplateRedisKey(DialogueRedisConstant.TEMPLATE_FLOW_NODE_INFO,session.getCallTemplateId(), targetFlowId);
        TemplateNodeInfo flowNode = getRedisObject(flowNodeKey, TemplateNodeInfo.class);
        session.setNodeType(flowNode.getNodeType());
        session.setFlowData(new DialogueFlowData(flowNode.getId(), flowNode.getNodeName(),flowNode.getNodeLabel(),flowNode.getLevelName()));
        //此时已经获取播报相关信息，把session 的相关信息重置
        session.setTargetFlowId(null);
        //重置为新的节点id，下一个交互时才能正常
        //将上一个节点置为当前节点
        session.setLastFlowId(session.getFlowId());
        session.setLastNodeType(session.getNodeType());
        //将当前节点置为目标节点
        session.setFlowId(targetFlowId);
        return TemplateFlowUtils.buildTemplateFlowActionData(session, flowNode, req.customerInfo());
    }
}
