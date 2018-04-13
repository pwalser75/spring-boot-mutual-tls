package ch.frostnova.spring.boot.mutual.tls.ws;

import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Weather client test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherClientTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @LocalServerPort
    private int port;

    @Test
    public void testGetWeather() {

        final String baseURL = "https://localhost:" + port;
        log.info("BASE URL: " + baseURL);
        try (final WeatherClient weatherClient = new WeatherClient(baseURL)) {
            List<DailyWeather> forecast = weatherClient.getForecast();
            Assert.assertNotNull(forecast);
        }
    }
}
