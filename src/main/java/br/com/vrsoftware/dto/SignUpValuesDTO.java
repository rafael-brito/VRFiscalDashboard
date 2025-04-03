package br.com.vrsoftware.dto;

import java.util.Objects;

public class SignUpValuesDTO {

    private String email;
    private String jiraToken;
    private String masterPassword;

    public SignUpValuesDTO() {
    }

    public SignUpValuesDTO (String email, String jiraToken, String masterPassword) {
        this.email = email;
        this.jiraToken = jiraToken;
        this.masterPassword = masterPassword;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJiraToken() {
        return this.jiraToken;
    }

    public void setJiraToken(String jiraToken) {
        this.jiraToken = jiraToken;
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
        SignUpValuesDTO that = (SignUpValuesDTO) o;
        return Objects.equals(email, that.email) && Objects.equals(jiraToken, that.jiraToken) && Objects.equals(masterPassword, that.masterPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, jiraToken, masterPassword);
    }
}
