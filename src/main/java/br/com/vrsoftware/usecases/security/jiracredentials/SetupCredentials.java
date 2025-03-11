package br.com.vrsoftware.usecases.security.jiracredentials;

import java.util.Scanner;

public class SetupCredentials {

    private static CredentialsEncryptor encryptor = new CredentialsEncryptor();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Jira e-mail: ");
        String username = scanner.nextLine();

        System.out.print("Enter Jira token: ");
        String password = scanner.nextLine();

        System.out.print("Enter master password for encryption (don't tell it to anyone): ");
        String masterPassword = scanner.nextLine();

        encryptor.createFile(masterPassword, username, password);

        System.out.println("Credentials setup completed successfully!");
    }
}
