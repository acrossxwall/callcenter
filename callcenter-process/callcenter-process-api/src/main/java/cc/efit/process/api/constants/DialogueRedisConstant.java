package cc.efit.process.api.constants;

/**
 * @author across
 * @Description
 * @Date 2025-08-31 10:49
 */

public class DialogueRedisConstant {
    /**
     * callId 对话会话信息
     */
    public static final String DIALOGUE_SESSION_KEY = "dialogue:session:%s";
    // 模板相关信息已template:%{id} 开头 便于清理redis缓存
    /**
     * 模板加载标志
     */
    public static final String TEMPLATE_INIT_REDIS = "template:%s:init";
    /**
     * 模板开始节点信息
     */
    public static final String TEMPLATE_START_NODE_INFO = "template:%s:start:node";
    /**
     * 模板节点信息
     */
    public static final String TEMPLATE_FLOW_NODE_INFO = "template:%s:flow:node:%s";
    /**
     * 模板意图信息 hash 结构
     */
    public static final String TEMPLATE_INTENTION_INFO = "template:%s:intention";
    /**
     * 意图分支信息 hash 结构
     */
    public static final String TEMPLATE_FLOW_BRANCH_INFO = "template:%s:flow:branch";
    /**
     * 模板知识库信息 hash 结构
     */
    public static final String TEMPLATE_KNOWLEDGE_INFO = "template:%s:knowledge";
    /**
     * 模板意向等级信息
     */
    public static final String TEMPLATE_INTENTION_LEVEL_INFO = "template:%s:intention:level";
    /**
     * 模板全局交互信息
     */
    public static final String TEMPLATE_GLOBAL_INTERACTION = "template:%s:global:interaction";
    /**
     * 模板全局无应答信息
     */
    public static final String TEMPLATE_GLOBAL_NO_REPLY = "template:%s:global:noreply";
    /**
     * 模板全局打断信息
     */
    public static final String TEMPLATE_GLOBAL_INTERRUPT = "template:%s:global:interrupt";
    /**
     * 模板全局NLU信息
     */
    public static final String TEMPLATE_GLOBAL_NLU_INFO = "template:%s:global:nlu";
    /**
     * 模板全局兜底话术
     */
    public static final String TEMPLATE_GLOBAL_DEFAULT_INFO = "template:%s:global:default";
}
