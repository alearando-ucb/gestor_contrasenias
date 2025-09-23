package com.ucb.amae.vault.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.file.Path;

import com.ucb.amae.vault.model.Vault;
import com.ucb.amae.vault.model.dto.VaultFile;

public class VaultFileIOService {

    public static final String VAULT_FILE_EXTENSION = ".aeav";
    private static final int ENCRYPTED_KEY_LENGTH = 48;

    public VaultFileIOService() {
    }

    public void writeVaultFile(Path filePath, VaultFile vaultFile) {
        try{
            DataOutputStream dos = new DataOutputStream(java.nio.file.Files.newOutputStream(filePath));

            dos.write(vaultFile.getSalt());
            dos.write(vaultFile.getKeyIV());
            dos.write(vaultFile.getDataIV());
            dos.write(vaultFile.getEncryptedKey());
            dos.write(vaultFile.getEncryptedData());

            dos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VaultFile loadVaultFile(Path filePath) {
        try{
            DataInputStream dis = new DataInputStream(java.nio.file.Files.newInputStream(filePath));

            byte[] salt = new byte[CipherService.SALT_LENGTH];
            dis.readFully(salt);
            byte[] keyIV = new byte[CipherService.IV_LENGTH];
            dis.readFully(keyIV);
            byte[] dataIV = new byte[CipherService.IV_LENGTH];
            dis.readFully(dataIV);
            byte[] encryptedKey = new byte[ENCRYPTED_KEY_LENGTH];
            dis.readFully(encryptedKey);
            byte[] encryptedData = new byte[dis.available()];
            dis.readFully(encryptedData);

            dis.close();

            return new VaultFile(salt, keyIV, dataIV, encryptedKey, encryptedData);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
