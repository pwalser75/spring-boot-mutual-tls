package ch.frostnova.spring.boot.mutual.tls.ws;

import ch.frostnova.spring.boot.mutual.tls.TLSClientConfig;
import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.security.KeyStore;
import java.util.List;

/**
 * Weather client API
 */
public class WeatherClient implements AutoCloseable {

    private final TLSClientConfig clientConfig;
    private final String baseURL;
    private final Client client;

    public WeatherClient(TLSClientConfig clientConfig, String baseURL) {
        this.clientConfig = clientConfig;
        this.baseURL = baseURL;
        ClientBuilder clientBuilder = createClientBuilder();
        client = clientBuilder.build();
    }

    public void close() {
        client.close();
    }

    private ClientBuilder createClientBuilder() {

        KeyStore keystore = clientConfig.getKeystore();
        KeyStore truststore = clientConfig.getTruststore();
        String keyPassword = clientConfig.getKeystoreKeyPassword();

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
    public List<DailyWeather> getForecast() {
        Invocation invocation = client
                .target(baseURL + "/api/weather/forecast")
                .request()
                .buildGet();

        Response response = ResponseExceptionMapper.check(invocation.invoke(), 200);
        return response.readEntity(new GenericType<List<DailyWeather>>() {
        });
    }
}
