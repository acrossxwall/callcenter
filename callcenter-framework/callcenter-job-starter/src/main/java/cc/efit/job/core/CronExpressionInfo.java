package cc.efit.job.core;

public record CronExpressionInfo(
        /**
         * cron 表达式
         */
        String cronExpression,
        /**
         * 1-启动 0-停止
         */
        int type
) {
    public static class Type {

        public static final int START = 1;

        public static final int STOP = 0;
    }
}
