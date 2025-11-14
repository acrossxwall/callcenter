package cc.efit.esl.core.event;

import cc.efit.esl.core.message.EslHeaders;
import cc.efit.esl.core.message.EslMessage;
import cc.efit.esl.core.utils.HeaderParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class EslEvent {
    private final Map<EslHeaders.Name, String> messageHeaders;
    private final Map<String, String> eventHeaders;
    private final List<String> eventBody;
    private final boolean decodeEventHeaders = true;

    public EslEvent(EslMessage rawMessage) {
        this(rawMessage, false);
    }

    public EslEvent(EslMessage rawMessage, boolean parseCommandReply) {
        messageHeaders = rawMessage.getHeaders();
        eventHeaders = new HashMap<>(rawMessage.getBodyLines().size());
        eventBody = new ArrayList<>();
        // plain or xml body
        if (rawMessage.getContentType().equals(EslHeaders.Value.TEXT_EVENT_PLAIN)) {
            parsePlainBody(rawMessage.getBodyLines());
        } else if (rawMessage.getContentType().equals(EslHeaders.Value.TEXT_EVENT_XML)) {
            throw new IllegalStateException("XML events are not yet supported");
        } else if (rawMessage.getContentType().equals(EslHeaders.Value.COMMAND_REPLY) && parseCommandReply) {
            parsePlainBody(rawMessage.getBodyLines());
        } else {
            throw new IllegalStateException("Unexpected EVENT content-type: " +
                    rawMessage.getContentType());
        }
    }


    public Map<EslHeaders.Name, String> getMessageHeaders() {
        return messageHeaders;
    }

    public Map<String, String> getEventHeaders() {
        return eventHeaders;
    }

    public List<String> getEventBodyLines() {
        return eventBody;
    }

    public String getEventName() {
        return getEventHeader(EslEventHeaderNames.EVENT_NAME);
    }

    public String getEventHeader(String header) {
        return getEventHeaders().get(header);
    }

    public long getEventSequence() {
        String sequence = getEventHeader(EslEventHeaderNames.EVENT_SEQUENCE);
        try {
            return Long.parseLong(sequence);
        }catch (Exception e){
            return 0;
        }
    }

    public String getCallerUuid() {
        return getEventHeaders().get(EslEventHeaderNames.CALLER_UNIQUE_ID);
    }

    public String getCallId() {
        return getEventHeaders().get(EslEventHeaderNames.VARIABLE_CALL_ID);
    }

    public long getEventDateTimestamp() {
        return Long.parseLong(getEventHeaders().get(EslEventHeaderNames.EVENT_DATE_TIMESTAMP));
    }

    public String getEventDateLocal() {
        return getEventHeaders().get(EslEventHeaderNames.EVENT_DATE_LOCAL);
    }

    public String getEventDateGmt() {
        return getEventHeaders().get(EslEventHeaderNames.EVENT_DATE_GMT);
    }

    public boolean hasEventBody() {
        return !eventBody.isEmpty();
    }

    private void parsePlainBody(final List<String> rawBodyLines) {
        boolean isEventBody = false;
        for (String rawLine : rawBodyLines) {
            if (!isEventBody) {
                // split the line
                String[] headerParts = HeaderParser.splitHeader(rawLine);
                if (decodeEventHeaders) {
                    try {
                        String decodedValue = URLDecoder.decode(headerParts[1], "UTF-8");
                        eventHeaders.put(headerParts[0], decodedValue);
                    } catch (UnsupportedEncodingException e) {
                        eventHeaders.put(headerParts[0], headerParts[1]);
                    }
                } else {
                    eventHeaders.put(headerParts[0], headerParts[1]);
                }
                if (headerParts[0].equals(EslEventHeaderNames.CONTENT_LENGTH)) {
                    isEventBody = true;
                }
            } else {
                if (rawLine.length() > 0) {
                    eventBody.add(rawLine);
                }
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EslEvent: name=[");
        sb.append(getEventName());
        sb.append("] headers=");
        sb.append(messageHeaders.size());
        sb.append(", eventHeaders=");
        sb.append(eventHeaders.size());
        sb.append(", eventBody=");
        sb.append(eventBody.size());
        sb.append(" lines.");
        return sb.toString();
    }
}
