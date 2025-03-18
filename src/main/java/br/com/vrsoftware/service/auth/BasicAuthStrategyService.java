package br.com.vrsoftware.service.auth;

import br.com.vrsoftware.dto.AuthCredentialsDTO;

import java.net.http.HttpRequest;

public class BasicAuthStrategyService implements IAuthenticationStrategy {

    private final AuthCredentialsDTO credentials;

    public BasicAuthStrategyService(AuthCredentialsDTO credentials) {
        this.credentials = credentials;
    }

    @Override
    public void applyAuthentication(HttpRequest.Builder requestBuilder) {
        requestBuilder.header("Authorization", credentials.getBasicAuthHeader());
    }
}
