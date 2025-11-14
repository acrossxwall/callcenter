package cc.efit.process.biz.handler.action;

import cc.efit.process.api.ProcessServiceProto;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.constants.DialogueRedisConstant;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.enums.flow.TemplateFlowStatusEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.handler.utils.TemplateFlowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("answerChannelAction")
@Slf4j
public class AnswerChannelAction  extends AbstractActionHandler {
    @Override
    public List<BaseActionData> actionHandle(DialogueProcessSession session, ChatProcessReq req) {
        String callId = req.callId();
        Integer callTemplateId = req.callTemplateId();
        log.info("answerChannelAction callId:{},callTemplateId:{}",callId, callTemplateId);
        session.setAnswerTime(LocalDateTime.now());
        String startNodeKey = FormatUtil.formatTemplateRedisKey(DialogueRedisConstant.TEMPLATE_START_NODE_INFO, callTemplateId);
        TemplateNodeInfo startNode = getRedisObject(startNodeKey, TemplateNodeInfo.class);
        log.info("当前call id:{},模板id:{},开始节点:{}",callId, callTemplateId, startNode);
        session.setFlowId(startNode.getId());
        session.setNodeType(startNode.getNodeType());
        session.setStatus(TemplateFlowStatusEnum.PROCESSING);
        session.setFlowData(new DialogueFlowData(startNode.getId(), startNode.getNodeName(),startNode.getNodeLabel(),startNode.getLevelName()));
        return TemplateFlowUtils.buildTemplateFlowActionData(session, startNode, req.customerInfo());
    }

    @Override
    public Integer getActionIndex() {
        return ProcessReqActionEnum.START.getCode();
    }
}
