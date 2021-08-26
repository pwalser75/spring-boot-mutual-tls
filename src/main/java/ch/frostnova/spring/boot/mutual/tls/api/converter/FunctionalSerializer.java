package ch.frostnova.spring.boot.mutual.tls.api.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.function.Function;

/**
 * JSON serializer that uses a converter function to serialize a value to a string when the value is not null
 * (otherwise, <code>null</code> will be serialized).
 */
public abstract class FunctionalSerializer<T> extends JsonSerializer<T> {

    private final Function<T, String> converter;

    /**
     * Constructor, expects a converter function.
     *
     * @param converter converter
     */
    protected FunctionalSerializer(Function<T, String> converter) {
        if (converter == null) {
            throw new IllegalArgumentException("converter is required");
        }
        this.converter = converter;
    }

    @Override
    public void serialize(T value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(converter.apply(value));
        }
    }
}
