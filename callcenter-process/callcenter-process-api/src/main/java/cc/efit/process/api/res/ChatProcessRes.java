package cc.efit.process.api.res;

import cc.efit.process.api.action.BaseActionData;
import lombok.Data;

import java.util.List;

@Data
public class ChatProcessRes {
    private String callId;
    private List<BaseActionData> actions;
    /**
     * 流程节点数据
     */
    private DialogueFlowData flowData;

    private MatchResult matchResult;
}
