package com.ucb.amae.vault.services;

import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucb.amae.vault.model.Vault;
import com.ucb.amae.vault.model.VaultEntry;
import com.ucb.amae.vault.model.dto.VaultFile;
import com.ucb.amae.vault.services.exceptions.DecryptionException;
import com.ucb.amae.vault.services.models.PasswordStrength;

public class VaultManagementService {

    private static Vault currenVault;
    private static byte[] masterKey;
    private static byte[] encryptedMasterKey;


    private VaultFileIOService vaultFileIOService;
    private CipherService cipherService;
    private JsonSerializationService jsonService;

    public VaultManagementService() {
        this.vaultFileIOService = new VaultFileIOService();
        this.cipherService = new CipherService();
        this.jsonService = new JsonSerializationService();
    }

    public void newVault(String password, Path filePath) {

        if(PasswordManagementService.evaluatePasswordStrength(password) != PasswordStrength.MUY_FUERTE){
            throw new IllegalArgumentException("La contraseña debil.");
        }

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
            VaultManagementService.encryptedMasterKey = encryptedKey;
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
            VaultManagementService.encryptedMasterKey = vaultFile.getEncryptedKey();

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
        } catch (DecryptionException e) {
            throw e;
        } catch (Exception e) {
            // Posible DecryptionException si la contraseña es incorrecta o archivo corrupto
            // TODO: Manejar la excepción apropiadamente
            e.printStackTrace();
        }
    }

    public void addEntryAndSave(VaultEntry entry, Path filePath) {
        try {
            if (currenVault == null || masterKey == null || encryptedMasterKey == null) {
                throw new IllegalStateException("No vault is currently loaded or created.");
            }
    
            /*
            * Agregamos la entrada al vault en memoria
            */
            currenVault.addEntry(entry);
    
            /*
            * Serializamos las entradas actualizadas a JSON
            */
            String data = jsonService.toJson(currenVault.getEntries());
    
            /*
            * Encriptamos los datos JSON con la llave maestra en memoria
            */
            byte[] encryptedData = cipherService.encrypt(data.getBytes(), masterKey, currenVault.getDataIV());
    
            /*
            * Creamos un DTO VaultFile con los nuevos datos encriptados
            */
            VaultFile vaultFile = new VaultFile(
                currenVault.getSalt(),
                currenVault.getKeyIV(),
                currenVault.getDataIV(),
                encryptedMasterKey,
                encryptedData
            );
    
            /*
            * Escribimos el archivo en el disco
            */
            vaultFileIOService.writeVaultFile(filePath, vaultFile);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Vault getCurrentVault() {
        return currenVault;
    }


}
