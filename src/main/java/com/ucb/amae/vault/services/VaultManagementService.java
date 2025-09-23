package com.ucb.amae.vault.services;

import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucb.amae.vault.model.Vault;
import com.ucb.amae.vault.model.dto.VaultFile;

public class VaultManagementService {

    private static Vault currenVault;
    private static byte[] masterKey;


    private VaultFileIOService vaultFileIOService;
    private CipherService cipherService;
    private JsonSerializationService jsonService;

    public VaultManagementService() {
        this.vaultFileIOService = new VaultFileIOService();
        this.cipherService = new CipherService();
        this.jsonService = new JsonSerializationService();
    }

    public void newVault(String password, Path filePath) {
        try{
            /*
            * Generamos los valores iniciales del vault
            */
            byte[] salt = cipherService.generateSalt();
            byte[] keyIV = cipherService.generateIV();
            byte[] dataIV = cipherService.generateIV();
            byte[] deriveKey = cipherService.deriveKey(password, salt);
            byte[] masterKey = cipherService.generateMasterKey();
            VaultManagementService.masterKey = masterKey;

            /*
            * Creamos el vault en memoria
            */
            VaultManagementService.currenVault = new Vault(salt, keyIV, dataIV);

            /*
            * Guardamos el vault en disco
            */
            byte[] encryptedKey = cipherService.encrypt(masterKey, deriveKey, keyIV);
            VaultFile vaultFile = new VaultFile(salt, keyIV, dataIV, encryptedKey, new byte[0]);

            String data = jsonService.toJson(VaultManagementService.currenVault.getEntries());
            byte[] encryptedData = cipherService.encrypt(data.getBytes(), masterKey, dataIV);
            vaultFile.setEncryptedData(encryptedData);
            vaultFileIOService.writeVaultFile(filePath, vaultFile);

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
