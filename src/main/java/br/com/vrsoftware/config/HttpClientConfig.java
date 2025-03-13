package br.com.vrsoftware.config;

import br.com.vrsoftware.service.security.proxy.SecureProxyLoaderService;

import java.net.*;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class HttpClientConfig {

    private static SecureProxyLoaderService secureProxyLoader = new SecureProxyLoaderService();
    private static final int TIMEOUT_SECONDS = 10;

    public static HttpClient createHttpClient() {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS));

        try {
            Properties proxyProperties = secureProxyLoader.secureLoad("vrdev");

            if (proxyProperties != null) {
                String proxyHost = proxyProperties.getProperty("http.proxyHost");
                String proxyPort = proxyProperties.getProperty("http.proxyPort");
                String proxyUser = proxyProperties.getProperty("http.proxyUser");
                String proxyPassword = proxyProperties.getProperty("http.proxyPassword");

                if (proxyHost != null && proxyPort != null) {
                    // Set up proxy
                    InetSocketAddress proxyAddress = new InetSocketAddress(
                            proxyHost, Integer.parseInt(proxyPort));

                    builder.proxy(ProxySelector.of(proxyAddress));

                    // Set up proxy authentication if credentials are provided
                    if (proxyUser != null && proxyPassword != null) {
                        final String user = proxyUser;
                        final String password = proxyPassword;

                        builder.authenticator(new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(user, password.toCharArray());
                            }
                        });

                        // Also set system properties as fallback
                        System.setProperty("http.proxyUser", proxyUser);
                        System.setProperty("http.proxyPassword", proxyPassword);
                        System.setProperty("https.proxyUser", proxyUser);
                        System.setProperty("https.proxyPassword", proxyPassword);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load proxy settings: " + e.getMessage());
            // Fallback to system proxy if available
            Optional<Proxy> systemProxy = detectSystemProxy();
            systemProxy.ifPresent(p -> {
                if (p.address() instanceof InetSocketAddress inetAddress) {
                    builder.proxy(ProxySelector.of(inetAddress));
                }
            });
        }

        return builder.build();
    }

    private static Optional<Proxy> detectSystemProxy() {
        List<Proxy> proxies = ProxySelector.getDefault().select(URI.create("http://example.com"));
        return proxies.stream()
                .filter(proxy -> proxy.type() == Proxy.Type.HTTP)
                .findFirst();
    }
}