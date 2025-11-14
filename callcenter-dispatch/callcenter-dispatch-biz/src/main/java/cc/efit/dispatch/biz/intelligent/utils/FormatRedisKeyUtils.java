package cc.efit.dispatch.biz.intelligent.utils;

public class FormatRedisKeyUtils {

    public static String formatIntelligentConcurrent(String key,Integer lineId,Integer deptId){
        return key.formatted(lineId,deptId);
    }
}
