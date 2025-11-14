package cc.efit.process.biz.base;

import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.dialogue.api.enums.TemplateIntentionTypeEnum;
import cc.efit.process.api.enums.KeywordTypeEnum;
import cc.efit.process.api.utils.FormatUtil;
import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;
import cc.efit.process.api.utils.KeywordClassifier;
import cc.efit.process.biz.cache.IntentionCache;
import cc.efit.process.biz.predict.keyword.Keyword;
import cc.efit.redis.utils.RedisUtils;

import java.util.*;

import static cc.efit.process.api.constants.DialogueRedisConstant.*;

/**
 * 获取一些公共类
 */
public interface CommonHandler {

    default <T> T getRedisObject(String key, Class<T> clazz) {
        return getRedisUtil().get(key, clazz);
    }

    default void setRedisJson(String key, String json) {
        getRedisUtil().set(key, json);
    }

    default RedisUtils getRedisUtil() {
        return SpringBeanHolder.getBean(RedisUtils.class);
    }

    default List<TemplateNodeBranchInfo> getFlowNodeBranches(Integer callTemplateId, Integer flowId) {
        List<TemplateNodeBranchInfo> branches = IntentionCache.nodeBranchCache.getIfPresent(flowId);
        if (branches == null || branches.isEmpty()) {
            branches = loadBranchesFromRedis(callTemplateId, flowId);
            IntentionCache.nodeBranchCache.put(flowId, branches);
        }
        return branches;
    }
    @SuppressWarnings("unchecked")
    default List<TemplateNodeBranchInfo> loadBranchesFromRedis(Integer callTemplateId, Integer flowId) {
        String branchKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_FLOW_BRANCH_INFO,callTemplateId);

        return (List<TemplateNodeBranchInfo>) getRedisUtil().hget(branchKey, flowId.toString() );
    }

    default List<Keyword> getIntentionKeywords(Integer callTemplateId, Integer intentionId) {
        List<Keyword> list = IntentionCache.intentionKeywordCache.getIfPresent(intentionId);
        if (list == null || list.isEmpty()) {
            list = loadIntentionKeywordsFromRedis(callTemplateId, intentionId);
            IntentionCache.intentionKeywordCache.put(intentionId, list);
        }
        return list;
    }

    default List<Keyword> loadIntentionKeywordsFromRedis(Integer callTemplateId, Integer intentionId) {
        String keywordKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_INTENTION_INFO,callTemplateId);
        Object keywords = getRedisUtil().hget(keywordKey, intentionId.toString());
        if (keywords==null) {
            return null;
        }
        List<Keyword> list = new ArrayList<>();
        String[] arr = keywords.toString().split("@");
        for (String keyword:arr){
            KeywordTypeEnum type = KeywordClassifier.classify(keyword);
            //能识别出来正则和逻辑表达式就按识别处理
            //不能识别出来就不管
            switch(type){
                case KeywordTypeEnum.EXPRESSION, KeywordTypeEnum.REGEX ->  list.add(new Keyword(type.getType(), keyword));
                default ->  {}
            }
        }
        return list;
    }

    default Integer getFlowDefaultBranch(Integer callTemplateId,Integer flowId) {
        List<TemplateNodeBranchInfo> branchInfos = getFlowNodeBranches(callTemplateId,flowId);
        return branchInfos.stream().filter(s-> TemplateIntentionTypeEnum.DEFAULT.getCode().equals(s.type())).findFirst().map(TemplateNodeBranchInfo::targetFlowId).orElse(null);
    }

    default Collection<TemplateKnowledgeVo> getAllKnowledge(Integer callTemplateId ) {
        Collection< TemplateKnowledgeVo> list = IntentionCache.knowledgeCache.getIfPresent(callTemplateId);
        if (list==null || list.isEmpty()) {
            list = loadKnowledgeVoFromRedis(callTemplateId);
            IntentionCache.knowledgeCache.put(callTemplateId,list);
        }
        return list;
    }

    default Collection<TemplateKnowledgeVo> loadKnowledgeVoFromRedis(Integer callTemplateId) {
        String knowledgeKey = FormatUtil.formatTemplateRedisKey(TEMPLATE_KNOWLEDGE_INFO,callTemplateId);
        List<Object> list = getRedisUtil().hvalues(knowledgeKey);
        return list.stream().map(s -> (TemplateKnowledgeVo) s).toList();
    }
}
