package cc.efit.dispatch.biz.job;

import cc.efit.call.api.constants.DispatchKeyConstants;
import cc.efit.dispatch.api.constant.CallRecordConstant;
import cc.efit.redis.utils.RedisUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CleanResourceJob {
    @Autowired
    private RedisUtils redisUtils;

    @XxlJob("releaseCallResource")
    public void releaseCallResource() {
        log.info("释放资源start");
        redisUtils.del(CallRecordConstant.CALL_RECORD_SET_KEY,
                DispatchKeyConstants.DISPATCH_CALL_START_KEY,DispatchKeyConstants.DISPATCH_REDIS_LOAD_DATA);
        redisUtils.scanDel("dispatch:task:*");
        redisUtils.scanDel("dispatch:adjust:*");
        log.info("释放资源end");
    }
}
