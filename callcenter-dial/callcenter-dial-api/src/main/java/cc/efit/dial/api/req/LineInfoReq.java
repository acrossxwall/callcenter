package cc.efit.dial.api.req;

public record LineInfoReq (
        /**
         * 线路ID
         */
        Integer lineId,
        /**
         * 线路名称
         */
        String lineName,
        /**
         * 主叫号码
         */
        String callNumber,
        /**
         * 拨打前缀
         */
        String callPrefix,
        /**
         * 是否是外部网关外呼
         */
        boolean gateway,
        /**
         * 外部网关名称
         */
        String gatewayName,
        /**
         * 外部网关地址
         */
        String realm,
        /**
         * 端口号
         */
        Integer port
){
}
