package br.com.vrsoftware.usecases.security;

import javax.crypto.SecretKey;

public interface ISecureLoader<T> {
    T secureLoad (String masterPassword);
    SecretKey generateKeyFromPassword (String password) throws Exception;
    String decrypt (byte[] encryptedData, SecretKey key) throws Exception;
}
