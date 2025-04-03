package br.com.vrsoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
//        AuthCredentialsDTO credentials = SecureCredentialsLoaderService
//                .loadSecureCredentials(args[0]);
//
//        DashboardApiClientService client = new DashboardApiClientService(credentials);
//
//        System.out.println(client.getIssue());

        SpringApplication.run(Main.class, args);
    }
}