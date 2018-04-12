package ch.frostnova.spring.boot.mutual.tls.api.converter;

import java.time.LocalTime;

/**
 * JAXB converter for {@link LocalTime}
 *
 * @author pwalser
 * @since 25.01.2018
 */
public final class LocalTimeConverter {

    public static class Serializer<LocalDate> extends FunctionalSerializer<LocalTime> {
        public Serializer() {
            super(value -> value == null ? null : value.toString());
        }
    }

    public static class Deserializer extends FunctionalDeserializer<LocalTime> {
        public Deserializer() {
            super(value -> value == null ? null : LocalTime.parse(value));
        }
    }
}
