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

    public void loadVault(String password, Path filePath) {
        try {
            /*
             * Cargar el archivo del vault desde disco
             */
            VaultFile vaultFile = vaultFileIOService.loadVaultFile(filePath);
            if (vaultFile == null) {
                // Opcional: manejar el caso de archivo no encontrado
                return;
            }

            /*
             * Derivar la llave de cifrado desde la contraseña y la salt
             * Descifrar la llave maestra con la llave derivada
             */
            byte[] deriveKey = cipherService.deriveKey(password, vaultFile.getSalt());
            byte[] masterKey = cipherService.decrypt(vaultFile.getEncryptedKey(), deriveKey, vaultFile.getKeyIV());
            VaultManagementService.masterKey = masterKey;

            /*
             * Descifrar los datos del vault con la llave maestra
             * Reconstruir el objeto Vault en memoria
             */
            byte[] jsonData = cipherService.decrypt(vaultFile.getEncryptedData(), masterKey, vaultFile.getDataIV());
            String json = new String(jsonData);

            VaultManagementService.currenVault = new Vault(
                vaultFile.getSalt(),
                vaultFile.getKeyIV(),
                vaultFile.getDataIV(),
                jsonService.fromJson(json)
            );

        } catch (JsonProcessingException e) {
            // Error al procesar el JSON (archivo corrupto)
            // TODO: Manejar la excepción apropiadamente
            e.printStackTrace();
        } catch (Exception e) {
            // Posible DecryptionException si la contraseña es incorrecta o archivo corrupto
            // TODO: Manejar la excepción apropiadamente
            e.printStackTrace();
        }
    }


}
