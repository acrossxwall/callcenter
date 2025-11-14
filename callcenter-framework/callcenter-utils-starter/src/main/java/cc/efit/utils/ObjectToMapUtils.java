package cc.efit.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectToMapUtils {
    
    /**
     * 将对象转换为 Map<String, String>
     * 使用反射获取所有字段值
     */
    public static Map<String, Object> convertToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            boolean accessible = field.canAccess(obj);
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    map.put(field.getName(), value);
                }
            } catch (IllegalAccessException ignore) {

            }finally {
                field.setAccessible(accessible);
            }
        }
        
        return map;
    }
}