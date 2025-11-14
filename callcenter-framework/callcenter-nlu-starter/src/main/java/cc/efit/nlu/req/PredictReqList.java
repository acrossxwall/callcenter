package cc.efit.nlu.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PredictReqList(
        List<String> texts,
        @JsonProperty("model_instance_name")
        String modelName,
        double threshold
) {
}
