package cc.efit.process.biz.handler.flow;

import cc.efit.core.enums.YesNoEnum;
import cc.efit.dialogue.api.enums.TemplateFlowTypeEnum;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.constants.DialogueRedisConstant;
import cc.efit.process.api.enums.flow.TemplateFlowIgnoreReplyTypeEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 有分支的开场白和普通节点进入此匹配
 * 和 JumpFlowHandler 区别在于是否需要匹配用户输入,是否匹配分支，只是为了找targetFlowId 或者去匹配知识库
 */
@Service("predictFlowHandler")
@Slf4j
public class PredictFlowHandler extends AbstractFlowHandler {
    @Override
    public List<BaseActionData> handler(DialogueProcessSession session, ChatProcessReq req) {
        Integer flowId = session.getFlowId();
        Integer nodeType = session.getNodeType();
        log.info("callId:{},current flow id:{}, flow node type:{}",req.callId(),flowId,nodeType);
        Integer targetFlowId ;
        if (TemplateFlowTypeEnum.START.getCode().equals(nodeType) ||
                TemplateFlowTypeEnum.NORMAL.getCode().equals(nodeType)) {
            log.info("callId:{},predict flow handler 预测",req.callId());
            //进行分支判断
            //判断是否忽略用户回复，如果忽略直接进行下一步知识库或者进行默认分支匹配
            //如果用户回复不忽略，则进行用户回复匹配,如果忽略
            //用户回复0:忽略用户回复 1:忽略用户除拒绝外的所有回复 2:都不忽略
            //TODO 目前  1忽略除拒绝外所有回复 还未实现
            String flowNodeKey = FormatUtil.formatTemplateRedisKey(DialogueRedisConstant.TEMPLATE_FLOW_NODE_INFO,session.getCallTemplateId(), flowId);
            TemplateNodeInfo flowNode = getRedisObject(flowNodeKey, TemplateNodeInfo.class);
            boolean ignoreReply = YesNoEnum.YES.getCode().equals(flowNode.getEnableIgnoreReply())
                    && TemplateFlowIgnoreReplyTypeEnum.IGNORE_REPLY.getType().equals(flowNode.getIgnoreReplyType());
            session.getMatchNodeLabels().add(flowNode.getNodeLabel());
            if (ignoreReply) {
                log.info("callId:{},忽略用户回复直接走默认分支或者去下一步",req.callId());
                if (YesNoEnum.YES.getCode().equals(flowNode.getEnableMatchKnowledge())  && !CollectionUtils.isEmpty(flowNode.getMatchKnowledgeIds())) {
                    //下一步开启知识库去知识库匹配预测
                    session.setMatchKnowledge(true);
                    session.setKnowledgeIds(flowNode.getMatchKnowledgeIds());
                }else{
                    //走默认分支连线对应的节点id
                    session.setMatchKnowledge(false);
                    targetFlowId = getFlowDefaultBranch(session.getCallTemplateId(), flowId);
                    session.setTargetFlowId(targetFlowId);
                }
                return null;
            }
            //匹配分支预测，如果匹配不到 且未开启知识库预测走默认分支
            //匹配到了则有flowId，走分支可能是普通节点也可能是跳转节点
            targetFlowId = predictFlow(req.content(), session, flowNode);
            if (targetFlowId==null) {
                //此时未预测到
                if (YesNoEnum.YES.getCode().equals(flowNode.getEnableMatchKnowledge())  && !CollectionUtils.isEmpty(flowNode.getMatchKnowledgeIds())) {
                    //下一步开启知识库去知识库匹配预测
                    session.setMatchKnowledge(true);
                    session.setKnowledgeIds(flowNode.getMatchKnowledgeIds());
                    return null;
                }else{
                    //走默认分支连线对应的节点id
                    session.setMatchKnowledge(false);
                    targetFlowId = getFlowDefaultBranch(session.getCallTemplateId(), flowId);
                }
            }
            session.setTargetFlowId(targetFlowId);
        }
        return null;
    }
}
