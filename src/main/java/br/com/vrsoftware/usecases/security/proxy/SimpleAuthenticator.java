package br.com.vrsoftware.usecases.security.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class SimpleAuthenticator extends Authenticator {

    private String username = "";
    private String password = "";

    public SimpleAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }
}
