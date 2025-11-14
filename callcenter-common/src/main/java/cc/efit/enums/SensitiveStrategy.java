package cc.efit.enums;

import java.util.function.Function;

/**
 * @author across
 * @Description
 * @Date 2025-09-08 20:48
 */

public enum SensitiveStrategy {
    MOBILE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
    ;

    Function<String,String> desensitive;
    SensitiveStrategy(Function<String,String> desensitive) {
        this.desensitive = desensitive;
    }
    public Function<String,String> getDesensitive() {
        return desensitive;
    }
}
