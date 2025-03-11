package br.com.vrsoftware.usecases.security.proxy;

import br.com.vrsoftware.exceptions.EncryptionException;
import br.com.vrsoftware.usecases.security.IEncryptor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ProxyEncryptor implements IEncryptor {
    
    SecureProxyLoader proxyLoader = new SecureProxyLoader();

    @Override
    public void createFile(String pMasterPassword, String... pParameters) {
        try {
            // Create the config directory if it doesn't exist
            Path configDir = Paths.get(System.getProperty("user.dir"), "config");
            Files.createDirectories(configDir);

            // Generate key from master password
            SecretKey key = proxyLoader.generateKeyFromPassword(pMasterPassword);

            // Prepare the credentials string
            String credentials = pParameters[0] + ":" + pParameters[1] + ":" + pParameters[2] + ":" + pParameters[3];

            // Encrypt the credentials
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(credentials.getBytes());
            String encoded = Base64.getEncoder().encodeToString(encryptedBytes);

            // Write to file
            Path credentialsPath = configDir.resolve("secure-proxy.dat");
            Files.write(credentialsPath, encoded.getBytes());

            System.out.println("Secure credentials file created at: " + credentialsPath);

        } catch (Exception e) {
            throw new EncryptionException("Error creating secure credentials file: " + e.getMessage(), e);
        }
    }
}
