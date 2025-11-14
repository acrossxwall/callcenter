package cc.efit.dispatch.biz.job;

import cc.efit.call.api.vo.line.DispatchLineVo;
import cc.efit.dispatch.biz.intelligent.IntelligentAdjustConcurrent;
import cc.efit.dispatch.biz.service.CallTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class CallTaskDispatchJob {
    @Autowired
    private CallTaskService callTaskService;
    @Autowired
    private IntelligentAdjustConcurrent intelligentAdjustConcurrent;

    @XxlJob("dispatchCallTask")
    public void dispatchCallTask() {
        log.info("调度并发任务start");
        log.info("先检测暂停任务");
        callTaskService.checkPauseTaskStatus();
        log.info("检测暂停任务结束");
        List<DispatchLineVo> lineVos = callTaskService.findRunningDispatchLineVoList();
        if (lineVos==null || lineVos.isEmpty()) {
            log.info("没有需要调度的拨打任务");
            return;
        }
        Set<DispatchLineVo> set = new HashSet<>(lineVos);
        intelligentAdjustConcurrent.adjustConcurrent(set);
        log.info("调度并发任务end");
    }
    @XxlJob("checkCallTaskStatus")
    public void checkCallTaskStatus() {
        callTaskService.checkCallTaskStatus();
    }
}
