package br.com.vrsoftware.Interface.service.security;

public interface IEncryptor {
    void createFile(String pMasterPassword, String... pParameters);
}