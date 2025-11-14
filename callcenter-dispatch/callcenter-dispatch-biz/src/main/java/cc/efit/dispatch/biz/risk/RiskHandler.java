package cc.efit.dispatch.biz.risk;

import cc.efit.call.api.domain.CallTask;
import cc.efit.dial.api.req.CallCustomerInfoReq;

public interface RiskHandler {

    boolean processCustomerRisk(CallCustomerInfoReq req, CallTask task);

    boolean strategyEnabled();

    RiskHandler getNextHandler();

    default boolean nextStrategyHandler(CallCustomerInfoReq req, CallTask task) {
        RiskHandler nextHandler = getNextHandler();
        return nextHandler!=null && nextHandler.processCustomerRisk(req, task);
    }
}
