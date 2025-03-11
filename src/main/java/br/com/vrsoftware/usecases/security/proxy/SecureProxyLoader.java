package br.com.vrsoftware.usecases.security.proxy;


import br.com.vrsoftware.entities.AuthProxy;
import br.com.vrsoftware.exceptions.AuthenticationException;
import br.com.vrsoftware.exceptions.EncryptionException;
import br.com.vrsoftware.usecases.security.ISecureLoader;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

public class SecureProxyLoader implements ISecureLoader<AuthProxy> {

    private static final String CREDENTIALS_FILE = "secure-proxy.dat";

    @Override
    public AuthProxy secureLoad(String masterPassword) {
        try {
            Path credentialsPath = Paths.get(System.getProperty("user.dir"), "config", CREDENTIALS_FILE);

            if (!credentialsPath.toFile().exists()) {
                throw new AuthenticationException("Credentials file not found");
            }

            // Read encrypted content
            byte[] encryptedData = Files.readAllBytes(credentialsPath);

            // Generate key from master password
            SecretKey key = generateKeyFromPassword(masterPassword);

            // Decrypt the data
            String decryptedContent = decrypt(encryptedData, key);

            // Parse the decrypted content
            String[] parts = decryptedContent.split(":");
            if (parts.length == 1 || parts.length > 5) {
                throw new AuthenticationException("Invalid credentials format");
            }

            return switch (parts.length) {
                case 2 -> new AuthProxy(parts[0], parts[1]);
                case 3 -> new AuthProxy(parts[0], parts[1], parts[2], "");
                case 4 -> new AuthProxy(parts[0], parts[1], parts[2], parts[3]);
                default -> new AuthProxy("", "");
            };

        } catch (Exception e) {
            throw new EncryptionException("Error loading secure credentials: " + e.getMessage(), e);
        }
    }

    @Override
    public SecretKey generateKeyFromPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        return new SecretKeySpec(hash, "AES");
    }

    @Override
    public String decrypt(byte[] encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
