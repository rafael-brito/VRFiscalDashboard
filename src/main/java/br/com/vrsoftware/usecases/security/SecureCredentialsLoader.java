package br.com.vrsoftware.usecases.security;

import br.com.vrsoftware.entities.AuthCredentials;
import br.com.vrsoftware.exceptions.AuthenticationException;
import br.com.vrsoftware.exceptions.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

public class SecureCredentialsLoader {
    private static final String CREDENTIALS_FILE = "secure-credentials.dat";

    public static AuthCredentials loadSecureCredentials(String masterPassword) {
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
            if (parts.length != 2) {
                throw new AuthenticationException("Invalid credentials format");
            }

            return new AuthCredentials(parts[0], parts[1]);

        } catch (Exception e) {
            throw new EncryptionException("Error loading secure credentials: " + e.getMessage(), e);
        }
    }

    protected static SecretKey generateKeyFromPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        return new SecretKeySpec(hash, "AES");
    }

    private static String decrypt(byte[] encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
