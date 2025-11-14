package cc.efit.json.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {


    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final DateTimeFormatter SHORT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LONG_FORMAT =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static {

        SimpleModule simpleModule = new SimpleModule();
        simpleModule
                // 新增 Long 类型序列化规则，数值超过 2^53-1，在 JS 会出现精度丢失问题，因此 Long 自动序列化为字符串类型
//                .addSerializer(Long.class, NumberSerializer.INSTANCE)
//                .addSerializer(Long.TYPE, NumberSerializer.INSTANCE)
                    .addSerializer(LocalDate.class, new LocalDateSerializer(SHORT_FORMAT))
                    .addDeserializer(LocalDate.class, new LocalDateDeserializer(SHORT_FORMAT))

                    .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(LONG_FORMAT))
                    .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(LONG_FORMAT));

        objectMapper.registerModule(simpleModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (text==null || text.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (text==null || text.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(text, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (text==null || text.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(text, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String,Object> parseObjectToMap(String text) {
        return   parseObject(text, new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
    }

}
