package br.com.vrsoftware.service.jira;

import br.com.vrsoftware.config.HttpClientConfig;
import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.exceptions.RequestException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BaseApiClientService {
    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper;
    protected final String baseUrl;
    protected final AuthCredentialsDTO authCredentials;

    protected BaseApiClientService(String baseUrl, AuthCredentialsDTO authCredentials) {
        this.httpClient = HttpClientConfig.createHttpClient();
        this.objectMapper = HttpClientConfig.createObjectMapper();
        this.baseUrl = baseUrl;
        this.authCredentials = authCredentials;
    }

    protected HttpRequest.Builder createRequestBuilder(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json");

        if (authCredentials != null) {
            builder.header("Authorization", authCredentials.getBasicAuthHeader());
        }
        return builder;
    }

    protected String handleResponse(HttpResponse<String> response) throws Exception {
//    protected <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) throws Exception {
        switch (response.statusCode()) {
            case 200:
            case 201:
                return response.body();
//                return objectMapper.readValue(response.body(), responseType);
            case 401:
                throw new RequestException("Erro de autenticação. Verifique suas credenciais.");
            case 403:
                throw new RequestException("Acesso negado. Você não tem permissão para acessar este recurso.");
            case 404:
                throw new RequestException("Recurso não encontrado.");
            default:
                throw new RequestException("Erro na requisição: " + response.statusCode() + " - " + response.body());
        }
    }
}
