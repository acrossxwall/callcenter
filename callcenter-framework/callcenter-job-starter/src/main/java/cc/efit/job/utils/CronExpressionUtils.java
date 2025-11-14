package cc.efit.job.utils;

import cc.efit.job.core.CronExpressionInfo;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cron表达式生成器
 */
public class CronExpressionUtils {

    /**
     * 根据允许日期生成复杂调度Cron表达式
     * @param allowDays 允许日期 "7,1,2,3,4,5,6"
     * @param allowTime 允许时间段 "09:00:00-21:00:00"
     * @param denyTime 禁止时间段 "12:00:00-13:00:00"
     * @return Cron表达式列表（开始和停止）
     */
    public static List<CronExpressionInfo> generateComplexCron(String allowDays, String allowTime,
                                                               String denyTime ) {
        List<CronExpressionInfo> cronExpressions = new ArrayList<>();
        String dayExpression = convertDaysToCron(allowDays);
        // 生成开始调度的Cron表达式
        String[] timeRange = parseTimeRange(allowTime);
        CronExpressionInfo startCron = generateStartCron( dayExpression, timeRange[0]);
        cronExpressions.add(startCron);
        // 生成停止调度的Cron表达式
        CronExpressionInfo stopCron = generateStopCron(dayExpression,timeRange[1]);
        cronExpressions.add(stopCron);

        // 如果有禁止时间段，生成暂停和恢复的Cron表达式
        if (denyTime != null && !denyTime.trim().isEmpty()) {
            String[] denyTimes = parseTimeRange(denyTime);
            CronExpressionInfo pauseCron = generateStopCron(allowDays, denyTimes[0]);
            CronExpressionInfo resumeCron = generateStartCron(allowDays, denyTimes[1]);
            cronExpressions.add(pauseCron);
            cronExpressions.add(resumeCron);
        }
        return cronExpressions;
    }

    /**
     * 生成开始外呼的Cron表达式
     */
    private static CronExpressionInfo generateStartCron( String dayExpression,String startTime) {
        return getCronExpressionInfo(startTime, dayExpression, CronExpressionInfo.Type.START);
    }

    /**
     * 生成停止外呼的Cron表达式
     */
    private static CronExpressionInfo generateStopCron(String dayExpression,String endTime) {
        return getCronExpressionInfo(endTime, dayExpression, CronExpressionInfo.Type.STOP);
    }

    private static CronExpressionInfo getCronExpressionInfo(String resumeTime, String dayExpression, int type) {
        LocalTime resume = parseTime(resumeTime);
        String cron = String.format("0 %d %d ? * %s",
                resume.getMinute(), resume.getHour(), dayExpression);
        return new CronExpressionInfo(cron, type);
    }

    /**
     * 将自定义日期格式转换为Cron星期表达式
     * @param allowDays "7,1,2,3,4,5,6"
     * @return Cron星期表达式 "SUN,MON,TUE,WED,THU,FRI,SAT"
     */
    public static String convertDaysToCron(String allowDays) {
        if (allowDays == null || allowDays.trim().isEmpty()) {
            return "*";
        }
        String[] days = allowDays.split(",");
        if (days.length == 0 || days.length >= 7) {
            return "*";
        }
        List<String> cronDays = new ArrayList<>();
        for (String day : days) {
            String cronDay = convertSingleDayToCron(day.trim());
            if (cronDay != null) {
                cronDays.add(cronDay);
            }
        }
        return cronDays.isEmpty() ? "*" : String.join(",", cronDays);
    }

    /**
     * 转换单个日期数字为Cron星期表示
     */
    private static String convertSingleDayToCron(String day) {
        return switch (day) {
            case "1" -> "1";
            case "2" -> "2";
            case "3" -> "3";
            case "4" -> "4";
            case "5" -> "5";
            case "6" -> "6";
            case "7" -> "7";
            default -> null;
        };
    }

    /**
     * 解析时间范围字符串
     */
    private static String[] parseTimeRange(String timeRange) {
        String[] times = timeRange.split("-");
        if (times.length != 2) {
            throw new IllegalArgumentException("时间范围格式错误: " + timeRange);
        }
        return new String[]{times[0].trim(), times[1].trim()};
    }

    /**
     * 解析时间字符串
     */
    private static LocalTime parseTime(String timeStr) {
        try {
            // 支持 HH:mm:ss 和 HH:mm 格式
            if (timeStr.length() == 8) {
                return LocalTime.parse(timeStr);
            } else if (timeStr.length() == 5) {
                return LocalTime.parse(timeStr + ":00");
            } else {
                throw new IllegalArgumentException("时间格式错误: " + timeStr);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("无效的时间格式: " + timeStr, e);
        }
    }

    private static void validateTimeRange(LocalTime start, LocalTime end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("开始时间必须早于结束时间");
        }
    }

    private static void validateInterval(int intervalMinutes) {
        if (intervalMinutes <= 0 || intervalMinutes > 60) {
            throw new IllegalArgumentException("执行间隔必须在1-60分钟之间");
        }
    }
}