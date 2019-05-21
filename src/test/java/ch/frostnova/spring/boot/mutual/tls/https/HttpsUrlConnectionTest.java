package ch.frostnova.spring.boot.mutual.tls.https;

import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import ch.frostnova.spring.boot.mutual.tls.ws.WeatherClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.net.ssl.*;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.List;

/**
 * HTTPS URL connection test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpsUrlConnectionTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDirectHTTPS() throws Exception {

        KeyStore keystore = loadKeystore("/test-client-keystore.p12", "Client-5ECr3T!");
        KeyStore truststore = loadKeystore("/test-client-truststore.p12", "Client-5ECr3T!");
        String keyPassword = "Client-5ECr3T!";

        URL url = new URL("https://localhost:" + port + "/api/weather/forecast");

        SSLContext sslContext = createSSLContext(keystore, keyPassword, truststore);
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        URLConnection urlConnection = getSecuredConnection(url, socketFactory);

        List<DailyWeather> weatherForecast = objectMapper.readValue(urlConnection.getInputStream(), new TypeReference<List<DailyWeather>>() {
        });

        System.out.println(objectMapper.writeValueAsString(weatherForecast));
    }

    public static URLConnection getSecuredConnection(URL url, SSLSocketFactory socketFactory) throws Exception {

        URLConnection connection = url.openConnection();
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection secureConnection = (HttpsURLConnection) connection;
            secureConnection.setConnectTimeout(500);
            secureConnection.setReadTimeout(1500);
            secureConnection.setSSLSocketFactory(socketFactory);
        }
        return connection;
    }

    public static SSLContext createSSLContext(KeyStore keyStore, String keyStorePassword, KeyStore truststore) throws GeneralSecurityException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword != null ? keyStorePassword.toCharArray() : null);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(truststore);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }

    private static KeyStore loadKeystore(String resource, String password) {
        try (InputStream in = WeatherClient.class.getResourceAsStream(resource)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(in, password.toCharArray());
            return keyStore;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load keystore '" + resource + "'", ex);
        }
    }
}
