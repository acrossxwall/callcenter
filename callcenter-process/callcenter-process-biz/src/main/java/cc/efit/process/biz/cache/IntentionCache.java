package cc.efit.process.biz.cache;

import cc.efit.dialogue.api.vo.knowledge.TemplateKnowledgeVo;
import cc.efit.dialogue.api.vo.node.TemplateNodeBranchInfo;
import cc.efit.process.biz.predict.keyword.Keyword;
import cc.efit.process.biz.predict.keyword.KeywordTrie;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.jexl3.JexlExpression;

import java.util.Collection;
import java.util.List;

/**
 * 意图缓存 关键词-意图
 */
public class IntentionCache {
    private IntentionCache(){}
    /**
     * 节点 意图分支缓存
     */
    public static final Cache<Integer, List<TemplateNodeBranchInfo>> nodeBranchCache = Caffeine .newBuilder()
            .maximumSize(1_000).build() ;


    /**
     * 模板级别 知识库缓存，没必要缓存那么大
     */
    public static final Cache<Integer, Collection<TemplateKnowledgeVo>> knowledgeCache = Caffeine .newBuilder() .maximumSize(100).build();
    /**
     * 意图 只需要逻辑表达式和正则表达式的关键词缓存
     */
    public static final Cache<Integer, List<Keyword>> intentionKeywordCache = Caffeine .newBuilder()
            .maximumSize(5_000).build() ;
    /**
     * 关键词 表达式缓存
     */
    public static final Cache<String, JexlExpression> expressionCache = Caffeine .newBuilder()
            .maximumSize(1_000).build() ;
    /**
     * 关键词树
     * 不区分模板，所有关键词都添加，不同意图在流程节点意图分支进行判断
     */
    public static final KeywordTrie trie = new KeywordTrie();

}
