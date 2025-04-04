package br.com.vrsoftware.service.security;

import br.com.vrsoftware.dto.SignUpValuesDTO;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class SetupCredentialsService {

    public void signup(SignUpValuesDTO signUpValues) {
        CredentialsEncryptorService.createSecureCredentialsFile(
            signUpValues.getEmail(),
            signUpValues.getJiraToken(),
            signUpValues.getMasterPassword()
        );
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Jira e-mail: ");
        String username = scanner.nextLine();

        System.out.print("Enter Jira token: ");
        String password = scanner.nextLine();

        System.out.print("Enter master password for encryption (don't tell it to anyone): ");
        String masterPassword = scanner.nextLine();

        CredentialsEncryptorService.createSecureCredentialsFile(username, password, masterPassword);

        System.out.println("Credentials setup completed successfully!");
    }
}
