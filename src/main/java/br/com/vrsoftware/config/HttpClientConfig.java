package br.com.vrsoftware.config;

import java.net.*;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class HttpClientConfig {
    private static final int TIMEOUT_SECONDS = 10;

    public static HttpClient createHttpClient() {
        Optional<Proxy> proxy = detectSystemProxy();
        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS));

        proxy.ifPresent(p -> builder.proxy(ProxySelector.of(p.address())));

        return builder.build();
    }

    private static Optional<Proxy> detectSystemProxy() {
        List<Proxy> proxies = ProxySelector.getDefault().select(URI.create("http://example.com"));
        return proxies.stream()
                .filter(proxy -> proxy.type() == Proxy.Type.HTTP)
                .findFirst();
    }
}
