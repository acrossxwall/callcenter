package cc.efit.notify.core;

public record PushResult (
    boolean success,
    String message,
    String channel
) {

}
