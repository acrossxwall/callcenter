package cc.efit.dial.biz.handler.status;

import cc.efit.dial.api.core.DialProcessSession;
import cc.efit.dial.api.enums.InteractiveRecordRoleEnum;
import cc.efit.dial.api.enums.ProcessSessionStatusEnum;
import cc.efit.dial.biz.handler.exception.ProcessActionException;
import cc.efit.dial.biz.service.ProcessService;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("customerInputStatusHandler")
@Slf4j
public class CustomerInputStatusHandler extends AbstractStatusHandler {
    @Autowired
    private ProcessService processService;
    @Override
    protected void doStatusHandler(DialProcessSession session) throws ProcessActionException {
        String callId = session.getCallId();
        List<String> result = session.getAsrResult();
        log.info("customer input status handler, callId:{},识别结果:{}", callId,result);
        String content = "";
        if (result!=null && !result.isEmpty()) {
            content = String.join("", result);
        }
        //添加交互记录客户输入播音内容
        addInteractiveRecord(session.getCallId(), InteractiveRecordRoleEnum.USER, content,null,null);
        ChatProcessReq req = buildChatProcessReq(session,content, ProcessReqActionEnum.CHAT);
        ChatProcessRes res = processService.requestProcessChat(req);
        handlerAction( session, res);
    }

    @Override
    public ProcessSessionStatusEnum currentStatus() {
        return ProcessSessionStatusEnum.CUSTOMER_INPUT;
    }
}
