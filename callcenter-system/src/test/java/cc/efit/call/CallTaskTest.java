package cc.efit.call;

import cc.efit.AppRun;
import cc.efit.call.api.repository.CallTaskRepository;
import cc.efit.call.api.vo.task.CallStatusCountInfo;
import cc.efit.call.biz.service.CallTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = AppRun.class)
public class CallTaskTest {
    @Autowired
    private CallTaskService taskService;
    @Autowired
    private CallTaskRepository taskRepository;
    @Test
    public void testCallTask() {
        Map<Integer,Long> map = taskService.getTaskCallStatusCountInfo();
        System.out.println(map);
    }
}
