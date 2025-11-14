package cc.efit.timer.core;

import cc.efit.timer.exception.TimerExistsException;

import java.util.concurrent.TimeUnit;

public interface TimerService {

    /**
     * 提交一个延迟任务
     * @param taskId      任务唯一ID（用于取消）
     * @param delay       延迟时间
     * @param unit        时间单位
     * @param onTimeout    回调（任务到期执行）
     * @param onCancel    可选：任务被取消时的回调
     * @throws TimerExistsException 任务ID已经添加上
     */
    void addDelayTask(String taskId, long delay, TimeUnit unit, Runnable onTimeout, Runnable onCancel) throws TimerExistsException;

    /**
     * 取消一个正在等待执行的任务
     * @param taskId 任务ID
     * @return true if cancelled
     */
    boolean cancel(String taskId);

    /**
     * 是否存在该任务
     */
    boolean contains(String taskId);

    /**
     * 停止定时器（应用关闭时调用）
     */
    void shutdown();

    /**
     * 获取当前待处理任务数
     */
    int size();
}
