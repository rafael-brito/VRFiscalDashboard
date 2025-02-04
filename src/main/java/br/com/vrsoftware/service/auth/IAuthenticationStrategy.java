package br.com.vrsoftware.service.auth;

import java.net.http.HttpRequest;

public interface IAuthenticationStrategy {
    void applyAuthentication(HttpRequest.Builder requestBuilder);
}
