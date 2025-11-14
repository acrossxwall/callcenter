package cc.efit.nlu.core;

import cc.efit.json.utils.JsonUtils;
import cc.efit.nlu.INluModelService;
import cc.efit.nlu.config.ModelProperties;
import cc.efit.nlu.req.PredictReq;
import cc.efit.nlu.req.PredictReqList;
import cc.efit.nlu.res.ApiResponse;
import cc.efit.nlu.res.ModelInfo;
import cc.efit.nlu.res.PredictItemResult;
import cc.efit.nlu.res.PredictRes;
import cc.efit.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
@RequiredArgsConstructor
public class NluModelServiceImpl implements INluModelService {
    private final ModelProperties properties;
    @Override
    public List<ModelInfo> listModelInfo() {
        ApiResponse<List<ModelInfo>> apiResponse  ;
        try {
            String json =  HttpUtils.get(properties.getUrl() + NluModelConfig.LIST_MODEL_INFO);
            apiResponse =  JsonUtils.parseObject(json, new TypeReference<>() {});
            return apiResponse.isSuccess()? apiResponse.getData():null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PredictItemResult predict(String modelId, String text,double threshold) {
        if (modelId==null) {
            throw new RuntimeException("模型id为空");
        }
        if (text==null) {
            throw new RuntimeException("文本为空");
        }
        if (threshold<=0 || threshold>1) {
            throw new RuntimeException("阈值不在0-1之间");
        }
        PredictReq req= new PredictReq(text,modelId,threshold);
        ApiResponse<PredictRes> apiResponse  ;
        try {
            String json =  HttpUtils.postJson(properties.getUrl() + NluModelConfig.PREDICT_MODEL, JsonUtils.toJsonString(req));
            apiResponse =  JsonUtils.parseObject(json, new TypeReference<>() {});
            PredictRes res =  apiResponse.getData();
            if (apiResponse.isSuccess()) {
                List<PredictItemResult> results = res.results();
                return results.stream().filter(PredictItemResult::passedThreshold).max(Comparator.comparingDouble(PredictItemResult::topScore)).orElse(null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<PredictItemResult> predict(String modelId, List<String> texts,double threshold) {
        if (modelId==null) {
            throw new RuntimeException("模型id为空");
        }
        if (texts==null || texts.isEmpty()) {
            throw new RuntimeException("文本为空");
        }
        if (threshold<=0 || threshold>1) {
            throw new RuntimeException("阈值不在0-1之间");
        }
        PredictReqList req= new PredictReqList(texts,modelId,threshold);
        ApiResponse<PredictRes> apiResponse  ;
        try {
            String json =  HttpUtils.postJson(properties.getUrl() + NluModelConfig.PREDICT_MODEL, JsonUtils.toJsonString(req));
            apiResponse =  JsonUtils.parseObject(json, new TypeReference<>() {});
            PredictRes res =  apiResponse.getData();
            if (apiResponse.isSuccess()) {
                return res.results();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
