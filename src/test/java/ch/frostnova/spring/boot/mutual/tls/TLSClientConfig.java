package ch.frostnova.spring.boot.mutual.tls;

import ch.frostnova.spring.boot.mutual.tls.ws.WeatherClient;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckString;

import java.io.IOException;
import java.security.KeyStore;
import java.util.Properties;

import static ch.frostnova.util.check.Check.required;
import static ch.frostnova.util.check.CheckString.notBlank;

public class TLSClientConfig {

    private final static String clientPropertiesResource = "client.properties";

    private final KeyStore keystore;
    private final KeyStore truststore;
    private final String keystoreKeyPassword;

    public TLSClientConfig(Properties properties) {
        var keystorePath = properties.getProperty("client.keystore");
        var keystorePassword = properties.getProperty("client.keystore.password", "");
        var truststorePath = properties.getProperty("client.truststore");
        var truststorePassword = properties.getProperty("client.truststore.password", "");
        keystoreKeyPassword = properties.getProperty("client.keystore.keypassword", "");
        required(keystorePath, "client.keystore", notBlank());
        required(keystorePath, "client.truststore", notBlank());

        keystore = TLSClientConfig.loadKeystore(TLSClientConfig.absoluteResource(keystorePath), keystorePassword);
        truststore = TLSClientConfig.loadKeystore(TLSClientConfig.absoluteResource(truststorePath), truststorePassword);
    }

    public static TLSClientConfig load() {
        return TLSClientConfig.load(TLSClientConfig.clientPropertiesResource);
    }

    public static TLSClientConfig load(String resource) {
        return new TLSClientConfig(TLSClientConfig.readProperties(resource));
    }

    private static Properties readProperties(String resource) {
        var properties = new Properties();
        try (var in = TLSClientConfig.class.getResourceAsStream(TLSClientConfig.absoluteResource(resource))) {
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
        try (var in = WeatherClient.class.getResourceAsStream(resource)) {
            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(in, password.toCharArray());
            return keyStore;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load keystore '" + resource + "'", ex);
        }
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
}
