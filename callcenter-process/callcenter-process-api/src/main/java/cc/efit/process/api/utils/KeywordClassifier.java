package cc.efit.process.api.utils;


import cc.efit.process.api.enums.KeywordTypeEnum;

import java.util.regex.Pattern;

public class KeywordClassifier {

    // 判断是否是逻辑表达式的关键字
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(
            "\\b(AND|OR|NOT|&&|\\|\\||!)\\b|contains\\s*\\(|matches\\s*\\(|\\w+\\s*\\(",
            Pattern.CASE_INSENSITIVE
    );

    // 常见正则元字符（排除单纯的点号 . ）
    private static final Pattern REGEX_PATTERN = Pattern.compile(
            "[\\\\\\[\\]{}()^$*+?|.]"
    );

    // 常见的正则特征（避免误判）
    private static final Pattern[] REGEX_HINTS = {
            Pattern.compile("\\\\[dDwWsS]"),     // \d, \w 等
            Pattern.compile("\\{\\d+,?\\d*}"), // {3}, {2,5}
            Pattern.compile("\\[.*?]"),        // 字符组
            Pattern.compile("\\(.*?\\)"),        // 分组
            Pattern.compile("[*+?]\\?"),         // 非贪婪
    };

    /**
     * 自动分类关键词类型
     */
    public static KeywordTypeEnum classify(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return KeywordTypeEnum.TEXT;
        }

        // 1. 先判断是否是逻辑表达式
        if (isLikelyExpression(keyword)) {
            return KeywordTypeEnum.EXPRESSION;
        }

        // 2. 再判断是否是正则
        if (isLikelyRegex(keyword)) {
            return KeywordTypeEnum.REGEX;
        }

        // 3. 默认是普通文本
        return KeywordTypeEnum.TEXT;
    }

    private static boolean isLikelyExpression(String kw) {
        return EXPRESSION_PATTERN.matcher(kw).find();
    }

    private static boolean isLikelyRegex(String kw) {
        // 如果包含大量特殊字符，且满足至少一个“正则特征”
        if (!REGEX_PATTERN.matcher(kw).find()) {
            return false;
        }

        // 排除纯文本中常见的“.”、“-”等情况
        if (kw.matches("[a-zA-Z0-9\\s\\-.@]+")) {
            return false; // 如邮箱、ID 等，不是正则
        }

        for (Pattern hint : REGEX_HINTS) {
            if (hint.matcher(kw).find()) {
                return true;
            }
        }

        // 特殊情况：以 ^ 或 $ 开头/结尾
        return kw.trim().startsWith("^") || kw.trim().endsWith("$");
    }
}