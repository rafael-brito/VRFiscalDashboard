package br.com.vrsoftware.entities;

import br.com.vrsoftware.usecases.security.proxy.SimpleAuthenticator;

import java.net.Authenticator;
import java.util.Base64;

public class AuthProxy {

    public static String ip = "";
    public static String port = "";
    public static String username = "";
    public static String password = "";

    public AuthProxy (
            String pIp,
            String pPort
    ) {
        ip = pIp;
        port = pPort;
        username = "";
        password = "";
    }

    public AuthProxy (
            String pIp,
            String pPort,
            String pUsername,
            String pPassword
    ) {
        ip = pIp;
        port = pPort;
        username = pUsername;
        password = pPassword;
    }

    public String getBasicAuthHeader() {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public static void setProxy() {
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", ip);
        System.setProperty("http.proxyPort", port);

        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", ip);
        System.setProperty("https.proxyPort", port);

        System.setProperty("ftp.proxySet", "true");
        System.setProperty("ftp.proxyHost", ip);
        System.setProperty("ftp.proxyPort", port);

        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

        Authenticator.setDefault(new SimpleAuthenticator(username, password));
    }
}
