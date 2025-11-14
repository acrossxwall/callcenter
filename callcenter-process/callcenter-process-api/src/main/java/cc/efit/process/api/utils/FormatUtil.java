package cc.efit.process.api.utils;

public class FormatUtil {

    public static String format(String format, Object... args) {
        return format.formatted( args);
    }

    public static String formatTemplateRedisKey(String formatRedisKey,Object... args) {
        return formatRedisKey.formatted(args);
    }
}
