package cc.efit.dialogue;

import cc.efit.AppRun;
import cc.efit.dialogue.biz.repository.TemplateFlowRepository;
import cc.efit.dialogue.biz.service.CallTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = AppRun.class)
public class TemplateTest {
    @Autowired
    private TemplateFlowRepository flowRepository;
    @Autowired
    private CallTemplateService callTemplateService;

    @Test
    public void testFlow(){
        int a = flowRepository.countByCallTemplateIdAndNodeName(18 ,"开场白");
        assert a == 1;
    }
    @Test
    public void loadTemplate() {
        Integer callTemplateId = 20;
        callTemplateService.initTemplateInfoToRedis(callTemplateId);
    }

}
