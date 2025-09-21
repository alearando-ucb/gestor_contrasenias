package com.ucb.amae.vault.services;

import java.security.SecureRandom;

import com.ucb.amae.vault.services.exceptions.CipherException;

public class CipherService {
    public static final int SALT_LENGTH = 16;

    private final SecureRandom secureRandom;

    public CipherService() {
        this.secureRandom = new SecureRandom();
    }

    public byte[] generateSalt() {
        try{
            byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
        }catch(Exception e){
            throw new CipherException("Error generating salt", e);
        }
    }
}
