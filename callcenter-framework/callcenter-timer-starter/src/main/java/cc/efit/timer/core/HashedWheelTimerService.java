package cc.efit.timer.core;

import cc.efit.timer.config.WheelTimerProperties;
import cc.efit.timer.exception.TimerExistsException;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class HashedWheelTimerService implements TimerService {

    private final Timer timer ;
    // 存储 taskId -> Timeout 映射
    private final Map<String, Timeout> pendingTimeouts;

    // 防止重复 shutdown
    private final AtomicBoolean shutdown  ;

    public HashedWheelTimerService(WheelTimerProperties properties) {
        timer = new HashedWheelTimer(
                new DefaultThreadFactory(properties.getPoolName()),
                properties.getTickDuration(),
                TimeUnit.MILLISECONDS,
                properties.getTicksPerWheel()
        );
        pendingTimeouts = new ConcurrentHashMap<>();
        shutdown = new AtomicBoolean(false);
    }

    @Override
    public void addDelayTask(String taskId, long delay, TimeUnit unit, Runnable onTimeout, Runnable onCancel) throws TimerExistsException {
        if (taskId == null || onTimeout == null) {
            throw new IllegalArgumentException("TaskId and onTimeout callback must not be null");
        }
        if (shutdown.get()) {
            throw new IllegalStateException("TimerService is already shutdown");
        }
        Timeout timeout = timer.newTimeout(t -> {
            try {
                if (t.isCancelled()) {
                    return;
                }
                onTimeout.run();
            } finally {
                // 无论成功与否，执行完就从 map 移除
                pendingTimeouts.remove(taskId);
            }
        }, delay, unit);

        // 包装带取消回调的代理 Timeout
        TimeoutWrapper wrapper = new TimeoutWrapper(timeout, onCancel);
        Timeout existing = pendingTimeouts.putIfAbsent(taskId, wrapper);
        if (existing != null) {
            // 已存在，取消本次提交的任务，避免泄漏
            timeout.cancel(); // 取消新创建的 timeout
            throw new TimerExistsException("Task with id '" + taskId + "' is already scheduled and not expired/cancelled.");
        }
    }

    @Override
    public boolean cancel(String taskId) {
        Timeout timeout = pendingTimeouts.remove(taskId);
        if (timeout != null) {
            return timeout.cancel();
        }
        return false;
    }

    @Override
    public boolean contains(String taskId) {
        return pendingTimeouts.containsKey(taskId);
    }

    @Override
    public int size() {
        return pendingTimeouts.size();
    }

    @Override
    public void shutdown() {
        if (shutdown.compareAndSet(false, true)) {
            timer.stop();
            pendingTimeouts.clear();
        }
    }
}