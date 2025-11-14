package cc.efit.dispatch.biz.risk;

import cc.efit.call.api.domain.CallTask;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dispatch.biz.risk.handler.BlackInfoRiskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拨号拦截的规则，链式处理
 */
@Component
public class RiskManagerHandlerChain {

    private final RiskHandler firstHandler;
    @Autowired
    public RiskManagerHandlerChain(BlackInfoRiskHandler blackInfoRiskHandler) {
        this.firstHandler = blackInfoRiskHandler;
        //有第二个直接在下面set next
    }

    public boolean riskManagerChain(CallCustomerInfoReq req, CallTask task){
        return firstHandler.processCustomerRisk(req,task);
    }
}
