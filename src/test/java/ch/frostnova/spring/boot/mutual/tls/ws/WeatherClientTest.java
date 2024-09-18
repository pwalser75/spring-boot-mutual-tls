package ch.frostnova.spring.boot.mutual.tls.ws;

import ch.frostnova.spring.boot.mutual.tls.TLSClientConfig;
import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Weather client test
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherClientTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @LocalServerPort
    private int port;

    private TLSClientConfig clientConfig;

    @BeforeEach
    public void init() {
        clientConfig = TLSClientConfig.load();
    }

    @Test
    void testGetWeather() {
        final var baseURL = "https://localhost:" + port;
        log.info("BASE URL: {}", baseURL);
        try (final var weatherClient = new WeatherClient(clientConfig, baseURL)) {
            var days = ThreadLocalRandom.current().nextInt(1, 10);
            var weatherForecast = weatherClient.getForecast(days);
            assertThat(weatherForecast).isNotEmpty().hasSize(days);
            assertThat(weatherForecast).extracting(DailyWeather::getDate)
                    .allSatisfy(date -> assertThat(date).isAfterOrEqualTo(LocalDate.now()));
            assertThat(weatherForecast).extracting(DailyWeather::getTemperatureCelsius)
                    .allSatisfy(temperature -> assertThat(temperature).isGreaterThanOrEqualTo(-273.15));
            assertThat(weatherForecast).extracting(DailyWeather::getWeatherCondition)
                    .allSatisfy(condition -> assertThat(condition).isNotNull());
        }
    }
}
