package cc.efit.job.core;

public record BasicXxlJob(
    /**
     * 执行器任务handler名称
     */
    String jobHandlerName,
    /**
     * 执行器主键ID
     */
    int jobGroup,
    /**
     * cron表达式
     */
    String cron,
    /**
     * 执行参数
     */
    String executorParam,
    /**
     * 任务描述
     */
    String jobDesc,
    /**
     * 报警邮件
     */
    String alarmEmail
) {
}
