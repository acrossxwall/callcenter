package cc.efit.dispatch;

import cc.efit.AppRun;
import cc.efit.call.api.vo.line.LineInfo;
import cc.efit.call.api.repository.LineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = AppRun.class)
public class LineTest {
    @Autowired
    private LineRepository lineRepository;

    @Test
    public void testLine(){
        List<LineInfo> lineInfos = lineRepository.selectAssignLineInfo();
        System.out.println(lineInfos);
    }
}
