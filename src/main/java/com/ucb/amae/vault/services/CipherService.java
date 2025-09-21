package com.ucb.amae.vault.services;

import java.security.SecureRandom;

import com.ucb.amae.vault.services.exceptions.CipherException;

public class CipherService {
    public static final int SALT_LENGTH = 16;
    public static final int IV_LENGTH = 12;
    public static final int MASTER_KEY_LENGTH = 32; // 256 bits

    private final SecureRandom secureRandom;

    public CipherService() {
        this.secureRandom = new SecureRandom();
    }

    /*
     * Genera un salt aleatorio de 16 bytes
     * @return byte[] El salt generado
     */
    public byte[] generateSalt() {
        try{
            byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
        }catch(Exception e){
            throw new CipherException("Error generando el salt", e);
        }
    }

    /*
     * Genera un vector de inicialización (IV) aleatorio de 12 bytes
     * Para usar el algoritmo AES-GCM
     * @return byte[] El vector de inicialización generado
     */
    public byte[] generateIV() {
        try{
            byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);
        return iv;
        }catch(Exception e){
            throw new CipherException("Error generando el IV", e);
        }
    }

    /*
     * Genera una llave maestra aleatoria de 32 bytes (256 bits)
     * @return byte[] La llave maestra generada
     */
    public byte[] generateMasterKey() {
        try{
            byte[] masterKey = new byte[MASTER_KEY_LENGTH];
        secureRandom.nextBytes(masterKey);
        return masterKey;
        }catch(Exception e){
            throw new CipherException("Error generando la llave maestra", e);
        }
    }


}
