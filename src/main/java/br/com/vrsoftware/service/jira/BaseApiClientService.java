package br.com.vrsoftware.service.jira;

import br.com.vrsoftware.config.HttpClientConfig;
import br.com.vrsoftware.config.ObjectMapperConfig;
import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.exceptions.RequestException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class BaseApiClientService {
    protected final HttpClient httpClient;
    protected final String baseUrl;

    protected BaseApiClientService(String baseUrl) {
        this.httpClient = HttpClientConfig.createHttpClient();
        this.baseUrl = baseUrl;
    }

    protected HttpRequest.Builder createRequestBuilder(String path, AuthCredentialsDTO credentials) {
        return createRequestBuilder(path, credentials, Optional.empty());
    }

    /**
     * Creates a request builder with authentication headers for the provided credentials
     */
    protected HttpRequest.Builder createRequestBuilder(String path, AuthCredentialsDTO credentials, Optional<String> jql) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header("Content-Type", "application/json");

        String uriString = baseUrl + path;

        if (jql.isPresent()) {
            URI uri = UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(path)
                    .queryParam("jql", jql.get())
                    .build()
                    .encode()
                    .toUri();

            builder.uri(uri);
        } else {
            builder.uri(URI.create(uriString));
        }

        if (credentials != null) {
            builder.header("Authorization", credentials.getBasicAuthHeader());
        }
        return builder;
    }

    protected <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) {
        return switch (response.statusCode()) {
            case 200, 201 -> ObjectMapperConfig.fromJson(response.body(), responseType);
            case 401 -> throw new RequestException("Erro de autenticação. Verifique suas credenciais.");
            case 403 -> throw new RequestException("Acesso negado. Você não tem permissão para acessar este recurso.");
            case 404 -> throw new RequestException("Recurso não encontrado.");
            default ->
                    throw new RequestException("Erro na requisição: " + response.statusCode() + " - " + response.body());
        };
    }
}
