package br.com.vrsoftware.usecases.security;

import java.util.List;

public interface IEncryptor {
    void createFile(String pMasterPassword, String... pParameters);
}
