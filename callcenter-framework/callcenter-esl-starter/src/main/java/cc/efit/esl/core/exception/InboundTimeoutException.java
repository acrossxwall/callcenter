package cc.efit.esl.core.exception;

public class InboundTimeoutException extends InboundClientException {

    public InboundTimeoutException(String message) {
        super(message);
    }

    public InboundTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
