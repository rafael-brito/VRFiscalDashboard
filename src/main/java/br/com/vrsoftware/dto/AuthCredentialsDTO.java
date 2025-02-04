package br.com.vrsoftware.dto;

import java.util.Base64;

public class AuthCredentialsDTO {
    private final String email;
    private final String token;

    public AuthCredentialsDTO(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getBasicAuthHeader() {
        String credentials = email + ":" + token;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
