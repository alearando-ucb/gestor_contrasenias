package com.ucb.amae.vault.model.dto;

public class VaultFile implements java.io.Serializable {

    private byte[] salt;
    private byte[] keyIV;
    private byte[] dataIV;
    private byte[] key;
    private byte[] encryptedData;

    public VaultFile() {
    }
    public VaultFile(byte[] salt, byte[] keyIV, byte[] dataIV, byte[] key, byte[] encryptedData) {
        this.salt = salt;
        this.keyIV = keyIV;
        this.dataIV = dataIV;
        this.key = key;
        this.encryptedData = encryptedData;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getKeyIV() {
        return keyIV;
    }

    public void setKeyIV(byte[] keyIV) {
        this.keyIV = keyIV;
    }

    public byte[] getDataIV() {
        return dataIV;
    }

    public void setDataIV(byte[] dataIV) {
        this.dataIV = dataIV;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }
    
}
