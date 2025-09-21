package com.ucb.amae.vault.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.ucb.amae.vault.services.exceptions.CipherException;
import com.ucb.amae.vault.services.exceptions.EncryptionException;

public class CipherService {
    public static final int SALT_LENGTH = 16;
    public static final int IV_LENGTH = 12;
    public static final int MASTER_KEY_LENGTH = 32;
    public static final int DERIVED_KEY_LENGTH = 256;

    private static final int PBKDF2_ITERATIONS = 600_000;
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String AES_ALGORITHM = "AES";

    private final SecureRandom secureRandom;
    private final SecretKeyFactory keyFactory;

    public CipherService() {
        try {
            this.secureRandom = new SecureRandom();
            this.keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new CipherException("Failed to initialize cryptographic components", e);
        }
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

    public byte[] deriveKey(String password, byte[] salt) {

        ValidationService.validatePasswordNotNull(password);
        ValidationService.validatePasswordNotEmpty(password);
        ValidationService.validateSaltNotNull(salt);
        ValidationService.validateSaltNotEmpty(salt);
        ValidationService.validateSaltLength(salt, SALT_LENGTH);

        try{
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, DERIVED_KEY_LENGTH);
            SecretKey secretKey = keyFactory.generateSecret(spec);
            byte[] derivedKey = secretKey.getEncoded();
            return derivedKey;

        }catch(Exception e){
            throw new CipherException("Error derivando la llave", e);
        }

    }

    public byte[] encrypt(byte[] data, byte[] key, byte[] iv){
        ValidationService.validateDataNotNull(data);
        ValidationService.validateDataNotEmpty(data);
        ValidationService.validateKeyNotNull(key);
        ValidationService.validateKeyNotEmpty(key);
        ValidationService.validateKeyLength(key, MASTER_KEY_LENGTH);
        ValidationService.validateIVNotNull(iv);
        ValidationService.validateIVNotEmpty(iv);
        ValidationService.validateIVLength(iv, IV_LENGTH);
        try{
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES_ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(IV_LENGTH*8, iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
            byte[] encryptedData = cipher.doFinal(data);
            return encryptedData;
        }
        catch(Exception e){
            throw new EncryptionException("Error en el proceso de encriptación", e);
        }
    }
}
