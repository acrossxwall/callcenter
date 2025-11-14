package cc.efit.process.api.res;


public record DialogueFlowData (
        Integer nodeId,
    /**
     * 节点名称
     */
    String nodeName,
    /**
     * 节点自定义标签
     */
    String nodeLabel,
    /**
     * 节点自定义意向等级
     */
    String levelName
){}
