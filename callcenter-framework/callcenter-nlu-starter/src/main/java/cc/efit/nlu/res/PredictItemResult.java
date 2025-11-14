package cc.efit.nlu.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PredictItemResult(
        /**
         * 输入文本
         */
        @JsonProperty("input_text")
        String text,
        /**
         * 最匹配意图
         */
        @JsonProperty("top_intent")
        String topIntent,
        /**
         * 最高得分
         */
        @JsonProperty("top_score")
        double topScore,
        /**
         * 是否通过阈值
         */
        @JsonProperty("passed_threshold")
        boolean passedThreshold,
        /**
         * 预测的意图列表
         */
        List<PredictCandidatesItem> candidates
) {
}
