package ch.frostnova.spring.boot.mutual.tls.api.converter;

import java.time.LocalDateTime;

/**
 * JAXB converter for {@link LocalDateTime}
 *
 * @author pwalser
 * @since 25.01.2018
 */
public final class LocalDateTimeConverter {

    public static class Serializer extends FunctionalSerializer<LocalDateTime> {
        public Serializer() {
            super(value -> value == null ? null : value.toString());
        }
    }

    public static class Deserializer extends FunctionalDeserializer<LocalDateTime> {
        public Deserializer() {
            super(value -> value == null ? null : LocalDateTime.parse(value));
        }
    }
}
