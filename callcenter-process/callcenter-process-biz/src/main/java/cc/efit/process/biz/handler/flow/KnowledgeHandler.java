package cc.efit.process.biz.handler.flow;

import cc.efit.dialogue.api.constants.TemplateConstants;
import cc.efit.dialogue.api.enums.TemplateFlowTypeEnum;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.flow.TemplateKnowledgeActionEnum;
import cc.efit.process.api.enums.flow.TemplateKnowledgeTypeEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.api.res.MatchResult;
import cc.efit.process.biz.handler.utils.TemplateKnowledgeUtils;
import cc.efit.process.biz.predict.BasePredictHandler;
import cc.efit.process.biz.predict.PredictHandlerFactory;
import cc.efit.process.biz.predict.keyword.Keyword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service("knowledgeHandler")
@Slf4j
@RequiredArgsConstructor
public class KnowledgeHandler extends AbstractFlowHandler {

    private final PredictHandlerFactory predictFactory;
    @Override
    public List<BaseActionData> handler(DialogueProcessSession session, ChatProcessReq req) {
        //到了知识库不管有没有开启都把 关键词意图id设置为空
        Set<Integer> intentionIds = session.getKeywordIntentionIds();
        session.setKeywordIntentionIds(null);
        if (!session.isMatchKnowledge()) {
            log.info("callId:{} not match knowledge or knowledgeIds is empty", req.callId());
            return null;
        }
        String content = req.content();
        log.info("callId:{},content:{} enter knowledge handler handler", req.callId(),content);
        //没有配置知识库或者知识库包含全选
        List<Integer> knowledgeIds = session.getKnowledgeIds();
        boolean matchAllKnowledge = CollectionUtils.isEmpty(knowledgeIds) || knowledgeIds.stream().anyMatch(id->id.equals(-1));
        Collection<TemplateKnowledgeVo> list = getAllKnowledge(req.callTemplateId());
        TemplateKnowledgeVo hitKnowledge = null;
        BasePredictHandler handler = predictFactory.getPredictHandler(TemplateConstants.PREDICT_TYPE_KEYWORD);
        MatchResult matchResult = session.getMatchResult();
        boolean nluMatch = matchResult!=null && matchResult.nluMatch();
        for (TemplateKnowledgeVo vo : list) {
            if ( matchAllKnowledge || knowledgeIds.contains(vo.getId()) ) {
                //全局或者需要匹配当前库
                log.info("callId:{} 开始匹配知识库knowledge id:{}", req.callId(), vo.getId());
                boolean match = intentionIds.contains(vo.getIntentionId());
                if (match) {
                    hitKnowledge = vo;
                    break;
                }

                //关键词
                List<Keyword> keywords = getIntentionKeywords(req.callTemplateId(), vo.getIntentionId());
                for (Keyword keyword : keywords) {
                    if (handler.matchLogic(keyword, content)) {
                        match = true;
                        //退出内层循环
                        break;
                    }
                }
                if (match) {
                    //外层循环也退出
                    hitKnowledge = vo;
                    break;
                }
                //上一步已经调用过nlu应该会返回label, 没有必要再次调用，直接匹配意图的label即可
                //nlu 训练将所有意图id和语料组织在一起训练，故此处匹配意图id就行
                if (nluMatch && matchResult.nluMatchCode().equals(vo.getIntentionId().toString())) {
                    log.info("callId:{} nlu match code:{} knowledge id:{}", req.callId(), matchResult.nluMatchCode(), vo.getId());
                    hitKnowledge = vo;
                    break;
                }
            }
        }
        if (hitKnowledge!=null) {
            log.info("callId:{} match knowledge id:{}", req.callId(), hitKnowledge.getId());
            int action = hitKnowledge.getAction();
            if (action == TemplateKnowledgeActionEnum.BACK_TO_MAIN_FLOW_PRE.getCode()) {
                //回到跳出主流程的上个节点
                session.setFlowId(session.getLastFlowId());
                session.setNodeType(session.getLastNodeType());
            }else if (action == TemplateKnowledgeActionEnum.SPECIFIED_NODE.getCode()) {
                session.setFlowId(hitKnowledge.getTargetFlowId());
                session.setNodeType(TemplateFlowTypeEnum.NORMAL.getCode());
            }
            if (Objects.equals(TemplateKnowledgeTypeEnum.BUSINESS.getType(), hitKnowledge.getType())) {
                //触发业务问题
                session.setBusinessKnowledge(session.getBusinessKnowledge()+1);
            }else if(Objects.equals(TemplateKnowledgeTypeEnum.GENERAL.getType(), hitKnowledge.getType())){
                //触发一般问题
                session.setGeneralKnowledge(session.getGeneralKnowledge()+1);
            }
            session.getMatchKnowledgeIds().add(hitKnowledge.getId());
            return  TemplateKnowledgeUtils.buildTemplateKnowledgeActionData(session,hitKnowledge,req.customerInfo());
        }
        return null;
    }
}
