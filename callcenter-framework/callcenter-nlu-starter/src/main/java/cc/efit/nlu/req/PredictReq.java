package cc.efit.nlu.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictReq(
    String text,
    @JsonProperty("model_instance_name")
    String modelName,
    double threshold
) {
}
