package cc.efit.dispatch.biz.job;

import cc.efit.dispatch.biz.service.CallTaskService;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CallStatisticsJob {
    @Autowired
    private CallTaskService callTaskService;

    /**
     * 任务级别统计  9：00-21：00 每 5分钟执行一次
     */
    @XxlJob("callTaskStatistics")
    public void callTaskStatistics() {
        String params = XxlJobHelper.getJobParam();
        log.info("统计信息定时任务start params:{}", params);
        callTaskService.statistics(params);
        log.info("统计信息定时任务end");
    }

    /**
     * 系统级别统计 9：00-21：00 1个小时执行一次
     */
    @XxlJob("callSystemStatistics")
    public void callSystemStatistics() {
        String params = XxlJobHelper.getJobParam();
        log.info("系统级别统计定时任务start params:{}", params);
        callTaskService.systemStatistics(params);
        log.info("系统级别统计定时任务end");
    }
}
