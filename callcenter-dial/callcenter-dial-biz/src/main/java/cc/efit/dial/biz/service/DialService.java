package cc.efit.dial.biz.service;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dial.api.req.DialPhoneReq;
import cc.efit.dial.api.req.LineInfoReq;
import cc.efit.process.api.res.TemplateGlobalSettingRes;

public interface DialService {

    void dialPhone(String msg);
    void dialPhone(DialPhoneReq req);
    DialProcessSession createDialogueProcessSession(String callId, LineInfoReq lineInfo,
                                                    CallCustomerInfoReq customer,
                                                    TemplateGlobalSettingRes settings) ;

}
