package cc.efit.timer.core;

import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

// 包装 Netty 的 Timeout，支持 cancel 回调
public class TimeoutWrapper implements Timeout {
    private final Timeout delegate;
    private final Runnable onCancel;

    public TimeoutWrapper(Timeout delegate, Runnable onCancel) {
        this.delegate = delegate;
        this.onCancel = onCancel != null ? onCancel : () -> {};
    }

    @Override
    public Timer timer() {
        return delegate.timer();
    }

    @Override
    public TimerTask task() {
        return delegate.task();
    }

    @Override
    public boolean isExpired() {
        return delegate.isExpired();
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean cancel() {
        boolean cancelled = delegate.cancel();
        if (cancelled) {
            onCancel.run();
        }
        return cancelled;
    }
}