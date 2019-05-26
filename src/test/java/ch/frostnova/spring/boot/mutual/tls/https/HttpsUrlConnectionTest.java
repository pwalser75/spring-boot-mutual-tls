package ch.frostnova.spring.boot.mutual.tls.https;

import ch.frostnova.spring.boot.mutual.tls.TLSClientConfig;
import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.net.ssl.*;
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

    private TLSClientConfig clientConfig;

    @Before
    public void init() {
        clientConfig = TLSClientConfig.load();
    }

    @Test
    public void testDirectHTTPS() throws Exception {

        KeyStore keystore = clientConfig.getKeystore();
        KeyStore truststore = clientConfig.getTruststore();
        String keyPassword = clientConfig.getKeystoreKeyPassword();

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
}
