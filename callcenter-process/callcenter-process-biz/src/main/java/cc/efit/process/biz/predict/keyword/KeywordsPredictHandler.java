package cc.efit.process.biz.predict.keyword;

import cc.efit.dialogue.api.enums.TemplateIntentionClassifyEnum;
import cc.efit.process.api.res.MatchResult;
import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;
import cc.efit.dialogue.api.vo.node.TemplateNodeInfo;
import cc.efit.process.biz.cache.IntentionCache;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.biz.predict.AbstractPredictHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static cc.efit.dialogue.api.constants.TemplateConstants.PREDICT_TYPE_KEYWORD;

@Service("keywordsPredictHandler")
@Slf4j
public class KeywordsPredictHandler extends AbstractPredictHandler {
    @Override
    public MatchResult predictFlow(String content,  TemplateNodeInfo flowNode,  DialogueProcessSession session) {
        String callId = session.getCallId();
        log.info("callId:{},content:{},进入关键词预测 ",callId,content );
        //根据节点node 所有意图分支升序排序
        // 根据意图分支的关键词匹配 只要匹配就进入改分支返回改分支对应的节点node id
        Integer callTemplateId = flowNode.getCallTemplateId();
        Integer flowId = flowNode.getId();
        content = normalize(content);
        List<TemplateNodeBranchInfo> branches = getFlowNodeBranches(callTemplateId, flowId);
        //关键树匹配的返回
        Set<Integer> set = IntentionCache.trie.search(content);
        log.info("callId:{},content:{},关键词匹配结果:{}",callId,content,set);

        if (!set.isEmpty()) {
            for (TemplateNodeBranchInfo branch : branches) {
                if (set.contains(branch.intentionId())) {
                    buildSessionIntentionClassify(session,branch.classify());
                    //匹配成功
                    log.info("callId:{},content:{},关键词匹配成功,进入节点:{}",callId,content,branch.targetFlowId());
                    return new MatchResult(branch.targetFlowId(),content, branch.intentionId(),branch.name(),true,false,null);
                }
            }
        }
        //匹配失败 匹配正则和逻辑表达式
        for (TemplateNodeBranchInfo branch : branches) {
            Integer intentionId = branch.intentionId();
            List<Keyword> keywords = getIntentionKeywords(callTemplateId, intentionId);
            for (Keyword keyword : keywords) {
                if (matchLogic(keyword, content)) {
                    //匹配成功
                    buildSessionIntentionClassify(session,branch.classify());
                    log.info("callId:{},content:{},命中的正则或者逻辑表达式:{}匹配成功,进入节点:{}",callId,content,keyword.value(),branch.targetFlowId());
                    return new MatchResult(branch.targetFlowId(),keyword.value(), branch.intentionId(),branch.name(),true,false,null);
                }
            }
        }
        //走到这里没有匹配成功，把关键字意图匹配set 缓存起来，知识库匹配时不需要在匹配一次
        session.setKeywordIntentionIds(set);
        return null;
    }

    @Override
    public String getHandlerType() {
        return PREDICT_TYPE_KEYWORD;
    }

}
