package ch.frostnova.spring.boot.mutual.tls.https;

import ch.frostnova.spring.boot.mutual.tls.ws.WeatherClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * HTTPS URL connection test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpsUrlConnectionTest {

    @LocalServerPort
    private int port;

    @Test
    public void testDirectHTTPS() throws Exception {

        KeyStore keystore = loadKeystore("/test-client-keystore.jks", "Client-5ECr3T!");
        KeyStore truststore = loadKeystore("/test-client-truststore.jks", "Client-5ECr3T!");
        String keyPassword = "Client-5ECr3T!";

        URL url = new URL("https://localhost:" + port + "/api/weather/forecast");

        URLConnection urlConnection = getSecuredConnection(url, keystore, keyPassword, truststore);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private static URLConnection getSecuredConnection(URL url, KeyStore keyStore, String keyStorePassword, KeyStore truststore) throws Exception {

        URLConnection connection = url.openConnection();
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection secureConnection = (HttpsURLConnection) connection;

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword != null ? keyStorePassword.toCharArray() : null);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(truststore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            secureConnection.setConnectTimeout(500);
            secureConnection.setReadTimeout(1500);
            secureConnection.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        return connection;
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