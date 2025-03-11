package br.com.vrsoftware.adapters.auth;

import br.com.vrsoftware.entities.AuthCredentials;
import java.net.http.HttpRequest;

public class BasicAuthStrategy implements IAuthenticationStrategy {

    private final AuthCredentials credentials;

    public BasicAuthStrategy(AuthCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public void applyAuthentication(HttpRequest.Builder requestBuilder) {
        requestBuilder.header("Authorization", credentials.getBasicAuthHeader());
    }
}
