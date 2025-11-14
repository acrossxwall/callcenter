package cc.efit.call.biz.valid;

import java.time.LocalDateTime;

public interface CallValidationService {
    /**
     * 校验是否可以外呼
     * @param callTime 外呼时间
     * @param config 外呼配置
     * @return 校验结果 true 表示可以外呼
     */
    boolean validateCallTime(LocalDateTime callTime, CallTimeConfig config);
}
