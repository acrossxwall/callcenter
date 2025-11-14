package cc.efit.nlu;

import cc.efit.nlu.res.ModelInfo;
import cc.efit.nlu.res.PredictItemResult;

import java.util.List;

public interface INluModelService {
    /**
     * 获取可用模型信息
     */
    List<ModelInfo> listModelInfo();


    PredictItemResult predict(String modelId, String text,double threshold);

    List<PredictItemResult> predict(String modelId, List<String> texts, double threshold);
}
