package cc.efit.esl.core.message;

public class EslHeaders {

    public enum Name {
        CONTENT_TYPE("Content-Type"),
        CONTENT_LENGTH("Content-Length"),
        REPLY_TEXT("Reply-Text"),
        JOB_UUID("Job-UUID"),
        SOCKET_MODE("Socket-Mode"),
        CONTROL("CONTROL"),
        ;

        private final String literal;

        Name(String literal) {
            this.literal = literal;
        }

        public static Name fromLiteral(String literal) {
            for (Name name : values()) {
                if (name.literal.equalsIgnoreCase(literal)) {
                    return name;
                }
            }

            return null;
        }

        public String literal() {
            return literal;
        }
    }

    public static final class Value {
        public static final String OK = "+OK";
        public static final String AUTH_REQUEST = "auth/request";
        public static final String API_RESPONSE = "api/response";
        public static final String COMMAND_REPLY = "command/reply";
        public static final String TEXT_EVENT_PLAIN = "text/event-plain";
        public static final String TEXT_EVENT_XML = "text/event-xml";
        public static final String TEXT_DISCONNECT_NOTICE = "text/disconnect-notice";
        public static final String TEXT_RUDE_REJECTION = "text/rude-rejection";
        public static final String ERR_INVALID = "-ERR invalid";

        private Value() {
        }
    }
}