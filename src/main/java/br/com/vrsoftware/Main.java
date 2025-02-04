package br.com.vrsoftware;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.service.jira.DashboardApiClientService;
import br.com.vrsoftware.service.security.SecureCredentialsLoaderService;

public class Main {

    public static void main(String[] args) {
        AuthCredentialsDTO credentials = SecureCredentialsLoaderService
                .loadSecureCredentials(args[0]);

        DashboardApiClientService client = new DashboardApiClientService(credentials);

        // Example get issue
//        System.out.println(client.getIssue());

        // Example get issue worklogs
//        System.out.println(client.getIssueWorklogs());

        // Example get worklog
        System.out.println(client.getWorklog());
    }
}