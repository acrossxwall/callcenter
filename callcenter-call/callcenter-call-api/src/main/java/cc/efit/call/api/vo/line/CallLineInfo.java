package cc.efit.call.api.vo.line;

public record CallLineInfo(
        Integer lineId,
        String realm,
        Integer port,
        String callNumber,
        String callPrefix,
        String lineName,
        String gatewayName
) {
}
