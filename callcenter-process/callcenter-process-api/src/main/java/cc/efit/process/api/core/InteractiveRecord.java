package cc.efit.process.api.core;

import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.res.MatchResult;

public record InteractiveRecord (
        Integer role,
        String content,
        String time,
        DialogueFlowData flowData,
        MatchResult matchResult
) {
}
