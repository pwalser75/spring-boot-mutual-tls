package ch.frostnova.spring.boot.mutual.tls.api.converter;

import java.time.LocalDate;

/**
 * JSON converter for {@link LocalDate}
 */
public final class LocalDateConverter {

    public static class Serializer<LocalDate> extends FunctionalSerializer<LocalDate> {
        public Serializer() {
            super(value -> value == null ? null : value.toString());
        }
    }

    public static class Deserializer extends FunctionalDeserializer<LocalDate> {
        public Deserializer() {
            super(value -> value == null ? null : LocalDate.parse(value));
        }
    }
}
