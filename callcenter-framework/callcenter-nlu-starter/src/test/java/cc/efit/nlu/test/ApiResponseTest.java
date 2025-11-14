package cc.efit.nlu.test;

import cc.efit.json.utils.JsonUtils;
import cc.efit.nlu.res.ApiResponse;
import cc.efit.nlu.res.ModelInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ApiResponseTest {


    @Test
    public  void testApiResponseParse() {
        ApiResponse<List<ModelInfo>> apiResponse  ;
        String json = """
                {
                	"code": 0,
                	"msg": "success",
                	"data": [
                		{
                			"name": "20251110_095357",
                			"model_name": "offline-tfidf",
                			"threshold": 0.55,
                			"mode": "tfidf",
                			"created_at": "2025-11-10 09:53:57"
                		},
                		{
                			"name": "20251110_093211",
                			"model_name": "offline-tfidf",
                			"threshold": 0.55,
                			"mode": "tfidf",
                			"created_at": "2025-11-10 09:32:11"
                		}
                	]
                }
                """;
        apiResponse =  JsonUtils.parseObject(json, new TypeReference<>() {  });
        apiResponse.getData().forEach(System.out::println);
    }
}
