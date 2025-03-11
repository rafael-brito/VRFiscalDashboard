package br.com.vrsoftware.usecases;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientConfig {
    private static final int TIMEOUT_SECONDS = 10;

    public static HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }
}
