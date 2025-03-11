package br.com.vrsoftware.frameworks;

import br.com.vrsoftware.entities.AuthCredentials;
import br.com.vrsoftware.adapters.DashboardApiClient;
import br.com.vrsoftware.entities.AuthProxy;
import br.com.vrsoftware.usecases.security.jiracredentials.SecureCredentialsLoader;
import br.com.vrsoftware.usecases.security.proxy.SecureProxyLoader;

public class Main {

    public static void main(String[] args) {
        SecureCredentialsLoader secureCredentialsLoader = new SecureCredentialsLoader();
        SecureProxyLoader secureProxyLoader = new SecureProxyLoader();

        AuthCredentials credentials = secureCredentialsLoader.secureLoad(args[0]);
        AuthProxy proxy = secureProxyLoader.secureLoad(args[0]);
        AuthProxy.setProxy();

        DashboardApiClient client = new DashboardApiClient(credentials, proxy);

        System.out.println(client.getIssue());
    }
}
