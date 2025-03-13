package br.com.vrsoftware.service.security;

import br.com.vrsoftware.exceptions.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Properties;

public class SecureProxyLoaderService {
    private static final String PROXY_FILE = "secure-proxy.dat";

    public static Properties loadSecureProxySettings(String masterPassword) {
        try {
            Path proxyPath = Paths.get(System.getProperty("user.dir"), "config", PROXY_FILE);

            if (!proxyPath.toFile().exists()) {
                throw new EncryptionException("Proxy settings file not found");
            }

            // Read encrypted content
            byte[] encryptedData = Files.readAllBytes(proxyPath);

            // Generate key from master password
            SecretKey key = generateKeyFromPassword(masterPassword);

            // Decrypt the data
            String decryptedContent = decrypt(encryptedData, key);

            // Parse the decrypted content
            Properties proxyProperties = new Properties();
            proxyProperties.load(new java.io.StringReader(decryptedContent));

            return proxyProperties;

        } catch (Exception e) {
            throw new EncryptionException("Error loading secure proxy settings: " + e.getMessage(), e);
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

    public static void applyProxySettings(Properties proxyProperties) {
        System.setProperty("http.proxyHost", proxyProperties.getProperty("http.proxyHost"));
        System.setProperty("http.proxyPort", proxyProperties.getProperty("http.proxyPort"));
        System.setProperty("https.proxyHost", proxyProperties.getProperty("https.proxyHost"));
        System.setProperty("https.proxyPort", proxyProperties.getProperty("https.proxyPort"));
        System.setProperty("http.proxyUser", proxyProperties.getProperty("http.proxyUser"));
        System.setProperty("http.proxyPassword", proxyProperties.getProperty("http.proxyPassword"));
    }
}
