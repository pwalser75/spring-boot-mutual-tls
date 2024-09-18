package ch.frostnova.spring.boot.mutual.tls.ws;

import ch.frostnova.spring.boot.mutual.tls.TLSClientConfig;
import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

import java.util.List;

/**
 * Weather client API
 */
public class WeatherClient implements AutoCloseable {

    private final GenericType<List<DailyWeather>> DAILY_WEATHER_LIST_TYPE = new GenericType<>() {
    };

    private final TLSClientConfig clientConfig;
    private final String baseURL;
    private final Client client;

    public WeatherClient(TLSClientConfig clientConfig, String baseURL) {
        this.clientConfig = clientConfig;
        this.baseURL = baseURL;
        var clientBuilder = createClientBuilder();
        client = clientBuilder.build();
    }

    public void close() {
        client.close();
    }

    private ClientBuilder createClientBuilder() {

        var keystore = clientConfig.getKeystore();
        var truststore = clientConfig.getTruststore();
        var keyPassword = clientConfig.getKeystoreKeyPassword();

        return ClientBuilder.newBuilder()
                .keyStore(keystore, keyPassword)
                .trustStore(truststore)
                .property(ClientProperties.CONNECT_TIMEOUT, 500)
                .property(ClientProperties.READ_TIMEOUT, 5000)
                .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING");

    }

    /**
     * Get the weather forecast
     *
     * @return forecast for the next few days
     */
    public List<DailyWeather> getForecast(int days) {
        var invocation = client
                .target(baseURL + "/api/weather/forecast")
                .queryParam("days", days)
                .request()
                .buildGet();

        var response = ResponseExceptionMapper.check(invocation.invoke(), 200);
        return response.readEntity(DAILY_WEATHER_LIST_TYPE);
    }
}
