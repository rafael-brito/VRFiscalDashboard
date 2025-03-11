package br.com.vrsoftware.usecases.security.proxy;

import java.util.Scanner;

public class SetUpProxy {

    private static ProxyEncryptor encryptor = new ProxyEncryptor();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Proxy IP (with dots): ");
        String ip = scanner.nextLine();

        System.out.print("Enter Proxy PORT: ");
        String port = scanner.nextLine();

        System.out.print("Enter Proxy USERNAME: ");
        String username = scanner.nextLine();

        System.out.print("Enter Proxy PASSWORD: ");
        String password = scanner.nextLine();

        System.out.print("Enter master password for encryption (don't tell it to anyone): ");
        String masterPassword = scanner.nextLine();

        encryptor.createFile(masterPassword, ip, port, username, password);

        System.out.println("Credentials setup completed successfully!");
    }
}
