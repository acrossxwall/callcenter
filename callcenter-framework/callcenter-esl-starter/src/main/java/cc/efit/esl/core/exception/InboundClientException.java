package cc.efit.esl.core.exception;

public class InboundClientException extends RuntimeException {
    public InboundClientException(String message) {
        super(message);
    }

    public InboundClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public InboundClientException(Throwable cause) {
        super(cause);
    }
}
