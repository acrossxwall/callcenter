package cc.efit.dial.test;

import cc.efit.dial.biz.DialApplication;
import cc.efit.dial.biz.service.DialService;
import cc.efit.dial.biz.service.ProcessService;
import cc.efit.process.api.enums.ProcessReqActionEnum;
import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DialApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestChatService {
    @Autowired
    private DialService dialService;
    @Autowired
    private ProcessService processService;

    @Test
    public void testGrpcService() {
        dialService.dialPhone("");
    }
    @Test
    public void testChatProcess() {
        String callId = "123456";
        ProcessReqActionEnum action =  ProcessReqActionEnum.START;
        ChatProcessReq req = new ChatProcessReq(
                action.getCode(), "", callId,  20 ,null
        );
        ChatProcessRes res = processService.requestProcessChat(req);
        System.out.println(res);
    }
}
