package cc.efit.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    /**
     * 将对象转换为 BigDecimal
     * @param obj 输入对象
     * @return 转换后的 BigDecimal
     */
    private static BigDecimal toBigDecimal(Object obj) {
        return switch (obj) {
            case BigDecimal bigDecimal -> bigDecimal;
            case Long l -> BigDecimal.valueOf(l);
            case Integer i -> BigDecimal.valueOf(i);
            case Double v -> new BigDecimal(String.valueOf(v));
            case null, default -> throw new IllegalArgumentException("Unsupported type");
        };
    }

    /**
     * 加法
     * @param a 加数
     * @param b 加数
     * @return 两个加数的和，保留两位小数
     */
    public static BigDecimal add(Object a, Object b) {
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        return bdA.add(bdB).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 减法
     * @param a 被减数
     * @param b 减数
     * @return 两数的差，保留两位小数
     */
    public static BigDecimal subtract(Object a, Object b) {
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        return bdA.subtract(bdB).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 乘法
     * @param a 乘数
     * @param b 乘数
     * @return 两个乘数的积，保留两位小数
     */
    public static BigDecimal multiply(Object a, Object b) {
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        return bdA.multiply(bdB).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 除法
     * @param a 被除数
     * @param b 除数
     * @return 两数的商，保留两位小数
     */
    public static BigDecimal divide(Object a, Object b) {
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        return bdA.divide(bdB, 2, RoundingMode.HALF_UP);
    }

    /**
     * 除法
     * @param a 被除数
     * @param b 除数
     * @param scale 保留小数位数
     * @return 两数的商，保留两位小数
     */
    public static BigDecimal divide(Object a, Object b, int scale) {
        if (a==null || b==null || a.equals(0) || b.equals(0)) {
            return BigDecimal.ZERO;
        }
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        return bdA.divide(bdB, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法 百分比，只保留整数
     * @param a 被除数
     * @param b 除数
     * @return 百分比，只保留整数部分
     */
    public static int dividePercent(Integer a, Integer b) {
        if (a==null || b==null || a==0 || b==0) {
            return 0;
        }
        return divide(a,b).multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 分转元
     * @param obj 分的金额
     * @return 转换后的元，保留两位小数
     */
    public static BigDecimal centsToYuan(Object obj) {
        BigDecimal cents = toBigDecimal(obj);
        return cents.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * 元转分
     * @param obj 元的金额
     * @return 转换后的分
     */
    public static Long yuanToCents(Object obj) {
        BigDecimal yuan = toBigDecimal(obj);
        return yuan.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValue();
    }

}