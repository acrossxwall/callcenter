package cc.efit.process.biz.predict.nlu;

import cc.efit.dialogue.api.vo.global.TemplateGlobalInteractionInfo;
import cc.efit.dialogue.api.vo.global.TemplateGlobalNluInfo;
import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;
import cc.efit.nlu.INluModelService;
import cc.efit.nlu.res.PredictItemResult;
import cc.efit.process.api.res.MatchResult;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.process.biz.predict.AbstractPredictHandler;
import cc.efit.process.biz.predict.keyword.Keyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.efit.dialogue.api.constants.TemplateConstants.PREDICT_TYPE_NLU;
import static cc.efit.process.api.constants.DialogueRedisConstant.TEMPLATE_GLOBAL_NLU_INFO;

@Service("nluPredictHandler")
@Slf4j
public class NluPredictHandler extends AbstractPredictHandler {
    @Autowired
    private INluModelService nluModelService;

    @Override
    public MatchResult predictFlow(String content,  TemplateNodeInfo flowNode, DialogueProcessSession session) {
        String callId = session.getCallId();
        log.info("callId:{},content:{},进入nlu预测 ",callId,content );
        //根据节点node 所有意图分支升序排序
        // 根据意图分支的关键词匹配 只要匹配就进入改分支返回改分支对应的节点node id
        Integer callTemplateId = flowNode.getCallTemplateId();
        Integer flowId = flowNode.getId();
        content = normalize(content);
        TemplateGlobalNluInfo nluInfo = getTemplateGlobalNluInfo(callTemplateId);
        if (nluInfo.enabled()) {
            try {
                boolean nluMatch = false;
                String topIntent = null;
                PredictItemResult predictResult = nluModelService.predict(content, nluInfo.modelName(),nluInfo.threshold());
                if (predictResult != null && predictResult.passedThreshold()) {
                    //通过阈值 匹配的最佳意图
                    nluMatch = true;
                    topIntent = predictResult.topIntent();
                    log.info("callId:{},content:{},nlu匹配的意图code逻辑表达式:{}匹配成功",callId,content,topIntent);
                    List<TemplateNodeBranchInfo> branches = getFlowNodeBranches(callTemplateId, flowId);
                    for (TemplateNodeBranchInfo branch : branches) {
                        //nlu 训练将所有意图id和语料组织在一起训练，故此处匹配意图id就行
                        if (branch.intentionId().toString().equals(topIntent)) {
                            //匹配成功
                            buildSessionIntentionClassify(session,branch.classify());
                            log.info("callId:{},content:{},进入节点:{}",callId,content,branch.targetFlowId());
                            return new MatchResult(branch.targetFlowId(),null,  branch.intentionId(),branch.name(),false, true, topIntent);
                        }
                    }
                }
                //只要预测到 top intent 就要增加 意图
                return new MatchResult(null,null, null,null,false, nluMatch, topIntent);
            }catch (Exception e){
                log.error(" nlu预测异常", e);
            }

        }
        return null;
    }

    @Override
    public String getHandlerType() {
        return PREDICT_TYPE_NLU;
    }

    private TemplateGlobalNluInfo getTemplateGlobalNluInfo(Integer callTemplateId) {
        String nluKey = FormatUtil.format(TEMPLATE_GLOBAL_NLU_INFO, callTemplateId);
        return getRedisObject(nluKey, TemplateGlobalNluInfo.class);
    }
}
