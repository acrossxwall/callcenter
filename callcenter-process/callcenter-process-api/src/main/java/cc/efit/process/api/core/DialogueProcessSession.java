package cc.efit.process.api.core;

import cc.efit.process.api.enums.flow.TemplateFlowStatusEnum;
import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.res.MatchResult;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class DialogueProcessSession {
    /**
     * 通话callId
     */
    private String callId;
    /**
     * 话术模板id
     */
    private Integer callTemplateId;
    /**
     * 当前流程id
     */
    private Integer flowId;
    /**
     * 匹配的目前流程节点id
     */
    private Integer targetFlowId;
    /**
     * 当前流程节点类型
     */
    private Integer nodeType;
    /**
     * 上个流程节点id
     */
    private Integer lastFlowId;
    /**
     * 上个流程节点类型
     */
    private Integer lastNodeType;
    /**
     * 交互轮次
     */
    private Integer interactiveCount;
    /**
     * 无应答次数
     */
    private Integer noReplyCount;
    /**
     * 呼叫时间
     */
    private LocalDateTime callTime;
    /**
     * 接听时间
     */
    private LocalDateTime answerTime;
    /**
     * 挂断时间
     */
    private LocalDateTime hangupTime;
    /**
     * 转接时间
     */
    private LocalDateTime transferTime;
    /**
     * 会话状态 0-初始 1-进行中 2-转人工 3-挂断
     */
    private TemplateFlowStatusEnum status;
    /**
     * 流程节点数据
     */
    private DialogueFlowData flowData;
    /**
     * 启用知识库预测
     */
    private boolean matchKnowledge;
    /**
     * 当前节点待匹配知识库id
     */
    private List<Integer> knowledgeIds;
    /**
     * 当前会话匹配到的知识库id
     */
    private Set<Integer> matchKnowledgeIds = new HashSet<>();

    /**
     * 当前会话匹配到的节点标签
     */
    private Set<String> matchNodeLabels = new HashSet<>();
    /**
     * 匹配节点信息
     */
    private MatchResult matchResult;
    /**
     * 关键词匹配意图ids
     */
    private Set<Integer> keywordIntentionIds;
    /**
     * 业务知识库次数
     */
    private Integer businessKnowledge;
    /**
     * 一般知识库次数
     */
    private Integer generalKnowledge;
    /**
     * 肯定次数
     */
    private Integer affirmCount;
    /**
     * 否定次数
     */
    private Integer negativeCount;
    /**
     * 拒绝次数
     */
    private Integer refuseCount;
    /**
     * 静默次数
     */
    private Integer silentCount;
}
