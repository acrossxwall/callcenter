package cc.efit.process.biz.api;

import cc.efit.process.api.ChatProcessVoipApi;
import cc.efit.process.api.enums.KeywordTypeEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;
import cc.efit.process.api.utils.KeywordClassifier;
import cc.efit.process.biz.cache.IntentionCache;
import cc.efit.process.biz.handler.ActionHandlerFactory;
import cc.efit.process.biz.handler.BaseActionHandler;
import cc.efit.process.biz.predict.keyword.Keyword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatProcessVoipApiImpl implements ChatProcessVoipApi {

    private final ActionHandlerFactory actionHandlerFactory;
    @Override
    public ChatProcessRes chatProcess(ChatProcessReq req) {
        log.info("ChatProcessVoipApiImpl chatProcess req:{}", req);
        BaseActionHandler handler = actionHandlerFactory.getHandler(req.action());
        ChatProcessRes res = handler.processActionHandle(req);
        log.info("ChatProcessVoipApiImpl chatProcess res:{}", res);
        return res;
    }

    @Override
    public void initTemplateInfoToLocalCache(Integer callTemplateId, Map<String, List<TemplateNodeBranchInfo>> branchMap, Map<String, String> intentionMap,Map<String, TemplateKnowledgeVo> knowledgeVoMap) {
        log.info("初始化模板:{}意图缓存到当前项目的内存当中",callTemplateId);
        branchMap.forEach((k,v)->
            IntentionCache.nodeBranchCache.put(Integer.valueOf(k), v)
        );
        intentionMap.forEach((k,v)->{
            //分割关键词
            Integer intentionId = Integer.valueOf(k);
            List<Keyword> list = new ArrayList<>();
            String[] arr = v.split("@");
            for (String keyword:arr){
                KeywordTypeEnum type = KeywordClassifier.classify(keyword);
                //能识别出来正则和逻辑表达式就按识别处理
                //不能识别出来就按关键词
                switch(type){
                    case EXPRESSION, REGEX ->  list.add(new Keyword(type.getType(), keyword));
                    default -> IntentionCache.trie.insertKeywords(keyword, intentionId);
                }
            }
            IntentionCache.intentionKeywordCache.put(intentionId, list);
        });
        IntentionCache.knowledgeCache.put(callTemplateId, knowledgeVoMap.values());
    }
}
