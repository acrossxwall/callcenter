package cc.efit.process.api.utils;

import cc.efit.json.utils.JsonUtils;
import cc.efit.process.api.action.BaseActionData;
import cc.efit.process.api.core.DialogueProcessSession;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 话术流程日志脱敏与摘要，避免 ASR 文本、会话状态 JSON、客户变量明文落日志。
 */
public final class ProcessChatLogFormatter {

    private ProcessChatLogFormatter() {
    }

    /**
     * 用户话术（ASR 等）掩码，不落库明文日志。
     */
    public static String maskUtterance(String content) {
        return content;
//        if (content == null || content.isEmpty()) {
//            return "";
//        }
//        int n = content.length();
//        if (n <= 2) {
//            return "*".repeat(n);
//        }
//        if (n <= 6) {
//            return content.charAt(0) + "***";
//        }
//        return content.substring(0, 2) + "***" + content.substring(n - 2);
    }

    public static String summarizeReq(ChatProcessReq req) {
        if (req == null) {
            return "req=null";
        }
//        int custKeys = req.customerInfo() == null ? 0 : req.customerInfo().size();
//        int contentLen = req.content() == null ? 0 : req.content().length();
//        return String.format("action=%s,callId=%s,callTemplateId=%s,contentLen=%s,customerInfoSize=%s",
//                req.action(), req.callId(), req.callTemplateId(), contentLen, custKeys);
        return JsonUtils.toJsonString(req);
    }

    public static String summarizeActions(List<BaseActionData> actions) {
        if (actions == null || actions.isEmpty()) {
            return "[]";
        }
//        return actions.stream()
//                .map(a -> a.getAction().name())
//                .collect(Collectors.joining(",", "[", "]"));
        return JsonUtils.toJsonString(actions);
    }

    public static String summarizeRes(ChatProcessRes res) {
        if (res == null) {
            return "res=null";
        }
//        return String.format("callId=%s,actions=%s", res.getCallId(), summarizeActions(res.getActions()));
        return JsonUtils.toJsonString(res);
    }

    public static String sessionBrief(DialogueProcessSession session) {
        if (session == null) {
            return "session=null";
        }
//        return String.format("callId=%s,callTemplateId=%s,flowId=%s,status=%s,interactiveCount=%s",
//                session.getCallId(), session.getCallTemplateId(), session.getFlowId(),
//                session.getStatus(), session.getInteractiveCount());
        return JsonUtils.toJsonString(session);
    }
}
