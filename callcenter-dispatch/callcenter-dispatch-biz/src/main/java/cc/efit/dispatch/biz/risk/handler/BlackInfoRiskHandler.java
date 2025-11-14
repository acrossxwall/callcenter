package cc.efit.dispatch.biz.risk.handler;

import cc.efit.call.api.domain.CallTask;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cc.efit.call.api.enums.CallCustomerStatusEnum.CustomerCallStatus.BLACK;
import static cc.efit.dispatch.api.constant.DispatchRedisKeyConstant.DISPATCH_BLACK_INFO;

@Service
@Slf4j
public class BlackInfoRiskHandler extends AbstractRiskHandler {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected boolean riskHandler(CallCustomerInfoReq req, CallTask task) {
        String phone = req.phone();
        Integer orgId = task.getOrgId();
        String callId = req.callId();
        log.info("black risk phone={},callId={}", phone, callId);
        String blackKey = DISPATCH_BLACK_INFO.formatted(orgId,phone);
        boolean black = redisUtils.hasKey(blackKey);
        if (black) {
            log.info("black risk phone={},callId={} is black", phone, callId);
            updateCallCustomerCalledStatus(req ,BLACK);
        }
        return black;
    }

    @Override
    public boolean strategyEnabled() {
        return true;
    }
}
