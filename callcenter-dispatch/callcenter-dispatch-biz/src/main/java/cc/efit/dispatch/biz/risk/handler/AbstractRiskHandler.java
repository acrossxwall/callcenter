package cc.efit.dispatch.biz.risk.handler;

import cc.efit.call.api.domain.CallTask;
import cc.efit.call.api.enums.CallCustomerStatusEnum;
import cc.efit.call.api.repository.CallCustomerRepository;
import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.dial.api.constant.DialKeyConstants;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dispatch.biz.mq.producer.MessageProducer;
import cc.efit.dispatch.biz.risk.RiskHandler;
import cc.efit.json.utils.JsonUtils;

public abstract class AbstractRiskHandler implements RiskHandler {

    private RiskHandler nextHandler;

    protected void setNextHandler(RiskHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean processCustomerRisk(CallCustomerInfoReq req, CallTask task) {
        if (!strategyEnabled() ) {
            return nextStrategyHandler(req, task);
        }
        return riskHandler(req, task) || nextStrategyHandler(req, task);
    }

    @Override
    public RiskHandler getNextHandler() {
        return nextHandler;
    }

    protected  void updateCallCustomerCalledStatus(CallCustomerInfoReq req, CallCustomerStatusEnum.CustomerCallStatus callStatus ) {
        Integer id = req.id();
        Integer taskId= req.taskId();
        DialProcessSession session = new DialProcessSession();
        session.setTaskId(taskId);
        session.setCallId(req.callId());
        session.setCustomer(req);
        //不需要释放并发
        session.setReleaseSemaphore(false);
        session.setRiskHandler(true);
        session.setCallStatus(callStatus.getStatus());
        sendCallRecordMessage(session);
        CallCustomerRepository customerRepository = SpringBeanHolder.getBean(CallCustomerRepository.class);
        customerRepository.updateCustomerCallStatus(id, callStatus.getStatus(),CallCustomerStatusEnum.CustomerStatus.FINISH.getStatus());
    }

    private void sendCallRecordMessage(DialProcessSession session) {
        MessageProducer producer = SpringBeanHolder.getBean(MessageProducer.class);
        producer.send(DialKeyConstants.DIAL_CALL_RECORD_QUEUE_KEY, JsonUtils.toJsonString(session));
    }

    protected abstract boolean riskHandler(CallCustomerInfoReq req, CallTask task) ;
}
