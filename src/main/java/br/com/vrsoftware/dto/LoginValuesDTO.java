package br.com.vrsoftware.dto;

import java.util.Objects;

public class LoginValuesDTO {

    private String email;
    private String masterPassword;

    public LoginValuesDTO() {
    }

    public LoginValuesDTO(String email, String masterPassword) {
        this.email = email;
        this.masterPassword = masterPassword;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMasterPassword() {
        return this.masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoginValuesDTO that = (LoginValuesDTO) o;
        return Objects.equals(email, that.email) && Objects.equals(masterPassword, that.masterPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, masterPassword);
    }
}
