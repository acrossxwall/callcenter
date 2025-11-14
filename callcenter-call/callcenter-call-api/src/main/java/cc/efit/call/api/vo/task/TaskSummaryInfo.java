package cc.efit.call.api.vo.task;

/**
 * 任务汇总信息
 * @param count         任务数量
 * @param statistics    当日运行中的任务数量
 */
public record TaskSummaryInfo (
        Long count,
        TaskStatisticsInfo statistics
){
}
