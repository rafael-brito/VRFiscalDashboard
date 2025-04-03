package br.com.vrsoftware.service.security;

import br.com.vrsoftware.enums.MapName;
import br.com.vrsoftware.exceptions.EncryptionException;
import br.com.vrsoftware.util.MapDB;
import org.mapdb.Serializer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

import static br.com.vrsoftware.service.security.SecureCredentialsLoaderService.generateKeyFromPassword;

public class CredentialsEncryptorService {
    public static void createSecureCredentialsFile(String email, String jiraToken, String masterPassword) {
        try (MapDB<String, String> mapDB = new MapDB<>(
            MapName.CREDENTIALS.getName(),
            Serializer.STRING,
            Serializer.STRING
        )) {
            // Generate key from master password
            SecretKey key = generateKeyFromPassword(masterPassword);

            // Prepare the credentials string
            String credentials = email + ":" + jiraToken;

            // Encrypt the credentials
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(credentials.getBytes());
            String encoded = Base64.getEncoder().encodeToString(encryptedBytes);

            // Save the credentials
            mapDB.put(email, encoded);
            MapDB.commit();
        } catch (Exception e) {
            MapDB.rollback();
            throw new EncryptionException("Error creating secure credentials file: " + e.getMessage(), e);
        }
    }
}
