package ch.frostnova.spring.boot.mutual.tls;

import ch.frostnova.spring.boot.mutual.tls.ws.WeatherClient;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckString;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Properties;

public class TLSClientConfig {

    private final static String clientPropertiesResource = "client.properties";

    private final KeyStore keystore;
    private final KeyStore truststore;
    private final String keystoreKeyPassword;

    public static TLSClientConfig load() {
        return TLSClientConfig.load(TLSClientConfig.clientPropertiesResource);
    }

    public static TLSClientConfig load(String resource) {
        return new TLSClientConfig(TLSClientConfig.readProperties(resource));
    }

    public TLSClientConfig(Properties properties) {

        String keystorePath = properties.getProperty("client.keystore");
        String keystorePassword = properties.getProperty("client.keystore.password", "");
        String truststorePath = properties.getProperty("client.truststore");
        String truststorePassword = properties.getProperty("client.truststore.password", "");
        keystoreKeyPassword = properties.getProperty("client.keystore.keypassword", "");
        Check.required(keystorePath, "client.keystore", CheckString.notBlank());
        Check.required(keystorePath, "client.truststore", CheckString.notBlank());

        keystore = TLSClientConfig.loadKeystore(TLSClientConfig.absoluteResource(keystorePath), keystorePassword);
        truststore = TLSClientConfig.loadKeystore(TLSClientConfig.absoluteResource(truststorePath), truststorePassword);
    }

    public KeyStore getKeystore() {
        return keystore;
    }

    public KeyStore getTruststore() {
        return truststore;
    }

    public String getKeystoreKeyPassword() {
        return keystoreKeyPassword;
    }

    private static Properties readProperties(String resource) {
        Properties properties = new Properties();
        try (InputStream in = TLSClientConfig.class.getResourceAsStream(TLSClientConfig.absoluteResource(resource))) {
            properties.load(in);
            return properties;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read resource " + resource, ex);
        }
    }

    private static String absoluteResource(String resourcePath) {
        return resourcePath == null || resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
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
