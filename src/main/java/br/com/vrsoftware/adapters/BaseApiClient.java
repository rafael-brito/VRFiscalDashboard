package br.com.vrsoftware.adapters;

import br.com.vrsoftware.entities.AuthCredentials;
import br.com.vrsoftware.entities.AuthProxy;
import br.com.vrsoftware.exceptions.RequestException;
import br.com.vrsoftware.usecases.HttpClientConfig;
import br.com.vrsoftware.usecases.ObjectMapperConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BaseApiClient {
    protected final HttpClient httpClient;
    protected final String baseUrl;
    protected final AuthCredentials authCredentials;
    protected final AuthProxy authProxy;

    protected BaseApiClient(String baseUrl, AuthCredentials authCredentials, AuthProxy pAuthProxy) {
        this.httpClient = HttpClientConfig.createHttpClient();
        this.baseUrl = baseUrl;
        this.authCredentials = authCredentials;
        this.authProxy = pAuthProxy;
    }

    protected HttpRequest.Builder createRequestBuilder(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json");

        if (authCredentials != null) {
            builder.headers(
                    "Authorization", authCredentials.getBasicAuthHeader(),
                    "Proxy-Authorization", authProxy.getBasicAuthHeader()
            );
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
