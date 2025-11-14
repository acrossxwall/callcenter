package cc.efit.dispatch.biz.service;

import cc.efit.call.api.domain.CallTask;
import cc.efit.call.api.vo.line.DispatchLineVo;

import java.util.List;

public interface CallTaskService {

    CallTask findCallTaskFromDb(Integer id);

    CallTask findCallTaskFromRedis(Integer id);

    CallTask findCallTaskFromDbOrRedis(Integer id);

    void saveCallTaskToRedis(CallTask callTask);

    void dispatchCallTask(Integer taskId);
    void pauseDispatchCallTask(Integer taskId);
    boolean loadWaitCallCustomer(Integer taskId,boolean start);

    void dispatchCallCustomer(Integer taskId);

    void releaseTaskDispatchConcurrencySemaphore(Integer taskId);

    void dispatchCallTaskLoadData(Integer integer);

    List<DispatchLineVo> findRunningDispatchLineVoList();

    /**
     * 报表统计
     * @param params    定时任务的参数 可以为空
     */
    void statistics(String params);

    /**
     * 检查暂停任务的并发释放
     */
    void checkPauseTaskStatus();

    /**
     * 检测拨打任务的状态
     */
    void checkCallTaskStatus();

    /**
     * 系统级别报表统计
     * @param params
     */
    void systemStatistics(String params);
}
