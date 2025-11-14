package cc.efit.call.api.vo.task;

public record SystemStatisticsInfo(
        /**
         *  已导入数据
         */
        Long importedData,

        /**
         * 已呼叫数量
         */
        Long calledData,
        /**
         * 接听数量
         */
        Long connectedData,
        /**
         * 呼叫时长
         */
        Long duration,
        Integer orgId,
        Integer deptId,
        Integer userId
) {
}
