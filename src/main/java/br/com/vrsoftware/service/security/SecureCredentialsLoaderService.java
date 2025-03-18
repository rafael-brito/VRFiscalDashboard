package br.com.vrsoftware.service.security;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.dto.LoginValuesDTO;
import br.com.vrsoftware.enums.MapName;
import br.com.vrsoftware.exceptions.AuthenticationException;
import br.com.vrsoftware.exceptions.EncryptionException;
import br.com.vrsoftware.util.MapDB;
import org.mapdb.Serializer;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class SecureCredentialsLoaderService {

    public static AuthCredentialsDTO loadSecureCredentials(LoginValuesDTO loginValues) {
        try (MapDB<String, String> mapDB = new MapDB<>(
            MapName.CREDENTIALS.getName(),
            Serializer.STRING,
            Serializer.STRING
        )) {
            if (!mapDB.containsKey(loginValues.getEmail())) {
                throw new AuthenticationException("Credentials not found");
            }

            // Read encrypted content
            String encryptedData = mapDB.get(loginValues.getEmail());

            // Generate key from master password
            SecretKey key = generateKeyFromPassword(loginValues.getMasterPassword());

            // Decrypt the data
            String decryptedContent = decrypt(encryptedData, key);

            // Parse the decrypted content
            String[] parts = decryptedContent.split(":");
            if (parts.length != 2) {
                throw new AuthenticationException("Invalid credentials format");
            }

            return new AuthCredentialsDTO(parts[0], parts[1]);

        } catch (Exception e) {
            throw new EncryptionException("Error loading secure credentials: " + e.getMessage(), e);
        }
    }

    protected static SecretKey generateKeyFromPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        return new SecretKeySpec(hash, "AES");
    }

    private static String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
