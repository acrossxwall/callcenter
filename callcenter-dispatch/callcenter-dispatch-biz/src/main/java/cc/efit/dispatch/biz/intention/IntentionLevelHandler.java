package cc.efit.dispatch.biz.intention;

import cc.efit.call.api.domain.CallRecord;
import cc.efit.process.api.core.DialogueProcessSession;

public interface IntentionLevelHandler {

    void processCallRecordIntentionLevel(Integer callTemplateId, DialogueProcessSession session, CallRecord record);
}
