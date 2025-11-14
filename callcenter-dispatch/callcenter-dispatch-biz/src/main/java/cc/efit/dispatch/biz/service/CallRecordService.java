package cc.efit.dispatch.biz.service;

import cc.efit.dial.api.core.DialProcessSession;

public interface CallRecordService {

    void handlerCallRecord(String json) throws Exception;

    void handlerCallRecord(DialProcessSession session) throws Exception ;
}