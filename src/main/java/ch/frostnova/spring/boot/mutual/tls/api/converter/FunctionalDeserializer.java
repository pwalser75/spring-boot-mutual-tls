package ch.frostnova.spring.boot.mutual.tls.api.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.function.Function;

/**
 * JSON deserializer that uses a converter function to deserialize a value when the value is not null (otherwise,
 * <code>null</code> will be deserialized).
 */
public abstract class FunctionalDeserializer<T> extends JsonDeserializer<T> {

    private Function<String, T> converter;

    /**
     * Constructor, expects a converter function.
     *
     * @param converter
     */
    protected FunctionalDeserializer(Function<String, T> converter) {
        if (converter == null) {
            throw new IllegalArgumentException("converter is required");
        }
        this.converter = converter;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        if (value == null) {
            return null;
        }
        return converter.apply(value);
    }
}
