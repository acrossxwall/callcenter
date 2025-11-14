package cc.efit.nlu.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModelInfo (
        /**
         * 模型名称，预测时传入此值
         */
        String name,
        /**
         * 后台训练所用模型名称
         */
        @JsonProperty("model_name")
        String modelName,
        /**
         * 阈值
         */
        String threshold,
        /**
         * 模型类型
         */
        String mode,
        /**
         * 创建时间
         */
        @JsonProperty("created_at")
        String createdAt
){
}
