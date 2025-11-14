package cc.efit.utils;

import java.util.regex.Pattern;

public class RegexUtils {

    public static final String MOBILE_REGEX = "^1[3-9]\\d{9}$";

    public static boolean matchContent(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content).find();
    }

    public static boolean isMobile(String mobile) {
        return mobile!= null && mobile.matches(MOBILE_REGEX);
    }
}