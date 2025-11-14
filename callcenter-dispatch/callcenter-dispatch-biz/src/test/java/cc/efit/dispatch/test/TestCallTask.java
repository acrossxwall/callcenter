package cc.efit.dispatch.test;

import cc.efit.call.api.domain.CallTask;
import cc.efit.dispatch.biz.DispatchApplication;
import cc.efit.dispatch.biz.service.CallTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DispatchApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestCallTask {
    @Autowired
    private CallTaskService taskService;
    @Test
    public void testFindTask() {
        Integer id = 3;
        CallTask task = taskService.findCallTaskFromDb(id);
        System.out.println(task);
    }
}
