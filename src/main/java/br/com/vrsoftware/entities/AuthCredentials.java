package br.com.vrsoftware.entities;

import java.util.Base64;

public class AuthCredentials {
    private final String email;
    private final String token;

    public AuthCredentials(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getBasicAuthHeader() {
        String credentials = email + ":" + token;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
