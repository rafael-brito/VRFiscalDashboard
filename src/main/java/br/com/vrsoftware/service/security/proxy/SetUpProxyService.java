package br.com.vrsoftware.service.security.proxy;

import java.util.Scanner;

public class SetUpProxyService {
    private static ProxyEncryptorService oProxyEncryptorService = new ProxyEncryptorService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter PROXY HOST: ");
        String host = scanner.nextLine();

        System.out.print("Enter PROXY PORT: ");
        String port = scanner.nextLine();

        System.out.print("Enter PROXY USERNAME: ");
        String username = scanner.nextLine();

        System.out.print("Enter PROXY PASSWORD: ");
        String password = scanner.nextLine();

        System.out.print("Enter master password for encryption (don't tell it to anyone): ");
        String masterPassword = scanner.nextLine();

        oProxyEncryptorService.createFile(masterPassword, host, port, username, password);

        System.out.println("Proxy setup completed successfully!");
    }
}
