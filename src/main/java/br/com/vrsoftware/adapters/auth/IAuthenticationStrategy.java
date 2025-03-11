package br.com.vrsoftware.adapters.auth;

import java.net.http.HttpRequest;

public interface IAuthenticationStrategy {
    void applyAuthentication(HttpRequest.Builder requestBuilder);
}
