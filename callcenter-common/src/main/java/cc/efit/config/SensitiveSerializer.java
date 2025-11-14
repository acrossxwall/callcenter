package cc.efit.config;

import cc.efit.annotation.Sensitive;
import cc.efit.enums.SensitiveStrategy;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * @author across
 * @Description
 * @Date 2025-09-08 20:47
 */

public class SensitiveSerializer  extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveStrategy strategy;
    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(strategy.getDesensitive().apply(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        Sensitive s = beanProperty.getAnnotation(Sensitive.class);
        if (s == null) {
            return serializerProvider.findValueSerializer(beanProperty.getType(),beanProperty);
        }
        this.strategy = s.strategy();
        return this;
    }
}
