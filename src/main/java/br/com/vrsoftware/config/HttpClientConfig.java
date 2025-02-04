package br.com.vrsoftware.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientConfig {
    private static final int TIMEOUT_SECONDS = 10;

    public static HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }

    public static ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }
}
