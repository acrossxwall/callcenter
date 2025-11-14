package cc.efit.call.api.vo.task;

public record TaskStatisticsInfo(
        /**
         * 运行中任务
         */
        Long runningTasks,
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
        Long connectedData
) {
}
