package cc.efit.esl.core.message;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class EslMessage {
    private final Map<EslHeaders.Name, String> headers = new EnumMap<>(EslHeaders.Name.class);
    private final List<String> body = new ArrayList<>();

    private Integer contentLength = null;
    public Map<EslHeaders.Name, String> getHeaders() {
        return headers;
    }

    public boolean hasHeader(EslHeaders.Name headerName) {
        return headers.containsKey(headerName);
    }

    public String getHeaderValue(EslHeaders.Name headerName) {
        return headers.get(headerName);
    }

    public boolean hasContentLength() {
        return headers.containsKey(EslHeaders.Name.CONTENT_LENGTH);
    }

    public Integer getContentLength() {
        if (contentLength != null) {
            return contentLength;
        }
        if (hasContentLength()) {
            contentLength = Integer.valueOf(headers.get(EslHeaders.Name.CONTENT_LENGTH));
        }
        return contentLength;
    }

    public String getContentType() {
        return headers.get(EslHeaders.Name.CONTENT_TYPE);
    }

    public List<String> getBodyLines() {
        return body;
    }

    void addHeader(EslHeaders.Name name, String value) {
        headers.put(name, value);
    }

    void addBodyLine(String line) {
        if (line == null) {
            return;
        }
        body.add(line);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EslMessage: contentType=[");
        sb.append(getContentType());
        sb.append("] headers=");
        sb.append(headers.size());
        sb.append(", body=");
        sb.append(body.size());
        sb.append(" lines.");

        return sb.toString();
    }

}
