package cc.efit.exception;

/**
 * 自定义 HTTP 异常
 */
public  class HttpException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    public HttpException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}