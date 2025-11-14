package cc.efit.dial.biz.handler.base;

import cc.efit.utils.DateUtil;
import cc.efit.core.utils.SpringBeanHolder;
import cc.efit.dial.api.constant.DialKeyConstants;
import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.process.api.core.InteractiveRecord;
import cc.efit.dial.api.enums.InteractiveRecordRoleEnum;
import cc.efit.dial.api.req.CallCustomerInfoReq;
import cc.efit.dial.biz.client.FsApiCommand;
import cc.efit.dial.biz.handler.ActionHandlerFactory;
import cc.efit.dial.biz.handler.EventHandlerFactory;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.json.utils.JsonUtils;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import cc.efit.process.api.res.DialogueFlowData;
import cc.efit.process.api.res.MatchResult;
import cc.efit.redis.utils.RedisUtils;

import java.util.List;

public interface CommonHandler {

    default RedisUtils getRedisUtils(){
        return SpringBeanHolder.getBean(RedisUtils.class);
    }

    default void addInteractiveRecord(String callId, InteractiveRecordRoleEnum role, String content,
                                      DialogueFlowData flowData,
                                      MatchResult matchResult) {
        InteractiveRecord record = new InteractiveRecord(role.getRole(), content, DateUtil.getCurrentTimeString(),flowData,matchResult);
        getRedisUtils().lSet(DialKeyConstants.DIAL_SESSION_INTERACTIVE_KEY.formatted(callId), JsonUtils.toJsonString(record));
    }

    default ActionHandlerFactory getActionHandlerFactory(){
        return SpringBeanHolder.getBean(ActionHandlerFactory.class);
    }

    default EventHandlerFactory getEventHandlerFactory(){
        return SpringBeanHolder.getBean(EventHandlerFactory.class);
    }

    default FsApiCommand getFsApiCommand(){
        return SpringBeanHolder.getBean(FsApiCommand.class);
    }

    default void hangupChannel(String callUuid) {
        getFsApiCommand().hangupChannel(callUuid);
    }

    default ChatProcessReq buildChatProcessReq(DialProcessSession session, String content,
                                                 ProcessReqActionEnum action) {
        CallCustomerInfoReq customer = session.getCustomer();
        return new ChatProcessReq(
                action.getCode(), content, session.getCallId(), customer.callTemplateId(),customer.customerInfo()
        );
    }

    default void handlerAction(DialProcessSession session, ChatProcessRes res) throws ProcessActionException {
        List<BaseActionData> list = res.getActions();
        for (BaseActionData s : list) {
            BaseActionHandler handler = getActionHandlerFactory().getActionHandler(s.getAction().getAction());
            handler.actionProcess(session, s,res.getFlowData(),res.getMatchResult());
        }
    }
}
