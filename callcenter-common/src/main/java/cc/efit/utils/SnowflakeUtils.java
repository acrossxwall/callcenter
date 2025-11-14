package cc.efit.utils;

import cn.hutool.core.lang.generator.SnowflakeGenerator;

public class SnowflakeUtils {

    private static SnowflakeGenerator generator = new SnowflakeGenerator(1L,1L);
    public static String nextId() {
        return String.valueOf(generator.next());
    }
}
