package br.com.vrsoftware.dto;

import java.util.Base64;
import java.util.Objects;

public class AuthCredentialsDTO {
    private final String email;
    private final String token;

    public AuthCredentialsDTO(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getBasicAuthHeader() {
        String credentials = email + ":" + token;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthCredentialsDTO that = (AuthCredentialsDTO) o;
        return Objects.equals(email, that.email) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, token);
    }
}
