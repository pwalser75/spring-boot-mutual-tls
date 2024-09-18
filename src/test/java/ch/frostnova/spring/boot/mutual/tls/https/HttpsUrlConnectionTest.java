package ch.frostnova.spring.boot.mutual.tls.https;

import ch.frostnova.spring.boot.mutual.tls.TLSClientConfig;
import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * HTTPS URL connection test
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpsUrlConnectionTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private TLSClientConfig clientConfig;

    private static URLConnection getSecuredConnection(URL url, SSLSocketFactory socketFactory) throws Exception {

        var connection = url.openConnection();
        if (connection instanceof HttpsURLConnection) {
            var secureConnection = (HttpsURLConnection) connection;
            secureConnection.setConnectTimeout(500);
            secureConnection.setReadTimeout(1500);
            secureConnection.setSSLSocketFactory(socketFactory);
        }
        return connection;
    }

    private static SSLContext createSSLContext(KeyStore keyStore, String keyStorePassword, KeyStore truststore) throws GeneralSecurityException {
        var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword != null && !keyStorePassword.isEmpty() ? keyStorePassword.toCharArray() : null);

        var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(truststore);

        var sslContext = SSLContext.getInstance("TLSv1.3");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }

    @BeforeEach
    void init() {
        clientConfig = TLSClientConfig.load();
    }

    @Test
    void testDirectHTTPS() throws Exception {

        var keystore = clientConfig.getKeystore();
        var truststore = clientConfig.getTruststore();
        var keyPassword = clientConfig.getKeystorePassword();

        var url = new URL("https://localhost:" + port + "/api/weather/forecast");

        var sslContext = createSSLContext(keystore, keyPassword, truststore);
        var socketFactory = sslContext.getSocketFactory();
        var urlConnection = getSecuredConnection(url, socketFactory);

        var weatherForecast = objectMapper.readValue(urlConnection.getInputStream(), new TypeReference<List<DailyWeather>>() {
        });

        assertThat(weatherForecast).isNotNull().isNotEmpty();
        assertThat(weatherForecast).extracting(DailyWeather::getDate)
                .allSatisfy(date -> assertThat(date).isAfterOrEqualTo(LocalDate.now()));
        assertThat(weatherForecast).extracting(DailyWeather::getTemperatureCelsius)
                .allSatisfy(temperature -> assertThat(temperature).isGreaterThanOrEqualTo(-273.15));
        assertThat(weatherForecast).extracting(DailyWeather::getWeatherCondition)
                .allSatisfy(condition -> assertThat(condition).isNotNull());
    }
}
