package br.com.vrsoftware.usecases;

import br.com.vrsoftware.entities.AuthProxy;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientConfig {
    private static final int TIMEOUT_SECONDS = 10;

    public static HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress(AuthProxy.ip, Integer.parseInt(AuthProxy.port))))
                .authenticator(Authenticator.getDefault())
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }
}
