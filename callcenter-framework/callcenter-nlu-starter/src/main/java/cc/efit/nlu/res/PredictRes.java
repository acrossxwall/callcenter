package cc.efit.nlu.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PredictRes(
        /**
         * 匹配阈值
         */
        @JsonProperty("threshold_used")
        float threshold,
        /**
         * 预测结果
         */
        List<PredictItemResult> results
) {
}
