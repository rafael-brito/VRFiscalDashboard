package br.com.vrsoftware.service.security.proxy;

import br.com.vrsoftware.Interface.service.security.ISecureLoader;
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

public class SecureProxyLoaderService implements ISecureLoader<Properties> {
    private static final String PROXY_FILE = "secure-proxy.dat";

    @Override
    public Properties secureLoad(String masterPassword) {
        try {
            Path proxyPath = Paths.get(System.getProperty("user.dir"), "config", PROXY_FILE);

            if (!proxyPath.toFile().exists()) {
                throw new EncryptionException("Proxy settings file not found", null);
            }

            // Read encrypted content
            byte[] encryptedData = Files.readAllBytes(proxyPath);

            // Generate key from master password
            SecretKey key = generateKeyFromPassword(masterPassword);

            // Decrypt the data
            String decryptedContent = decrypt(encryptedData, key);

            String[] parts = decryptedContent.split(":");

            if (parts.length < 4) {
                throw new EncryptionException("Invalid proxy settings format", null);
            }

            Properties proxyProperties = new Properties();
            proxyProperties.setProperty("http.proxyHost", parts[0]);
            proxyProperties.setProperty("https.proxyHost", parts[0]);
            proxyProperties.setProperty("http.proxyPort", parts[1]);
            proxyProperties.setProperty("https.proxyPort", parts[1]);
            proxyProperties.setProperty("http.proxyUser", parts[2]);
            proxyProperties.setProperty("https.proxyUser", parts[2]);
            proxyProperties.setProperty("http.proxyPassword", parts[3]);
            proxyProperties.setProperty("https.proxyPassword", parts[3]);

            return proxyProperties;

        } catch (Exception e) {
            throw new EncryptionException("Error loading secure proxy settings: " + e.getMessage(), e);
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
