package com.ucb.amae.vault.services;

import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucb.amae.vault.model.Vault;
import com.ucb.amae.vault.model.VaultEntry;
import com.ucb.amae.vault.model.dto.VaultFile;
import com.ucb.amae.vault.services.exceptions.CipherException;
import com.ucb.amae.vault.services.exceptions.DecryptionException;
import com.ucb.amae.vault.services.exceptions.EncryptionException;
import com.ucb.amae.vault.services.models.PasswordStrength;

import java.io.IOException;

public class VaultManagementService {

    private static Vault currenVault;
    private static byte[] masterKey;
    private static byte[] encryptedMasterKey;
    private static String currentVaultFileName;
    private static Path currentVaultPath;

    private VaultFileIOService vaultFileIOService;
    private CipherService cipherService;
    private JsonSerializationService jsonService;
    private ConfigurationsService configurationsService;

    public VaultManagementService() {
        this.vaultFileIOService = new VaultFileIOService();
        this.cipherService = new CipherService();
        this.jsonService = new JsonSerializationService();
        this.configurationsService = new ConfigurationsService();
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
            VaultManagementService.currentVaultFileName = filePath.getFileName().toString();
            VaultManagementService.currentVaultPath = filePath;

            configurationsService.saveLastVault(filePath.toString(), filePath.getFileName().toString());

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
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
            VaultManagementService.currentVaultFileName = filePath.getFileName().toString();
            VaultManagementService.currentVaultPath = filePath;

            configurationsService.saveLastVault(filePath.toString(), filePath.getFileName().toString());

        } catch (JsonProcessingException e) {
            // Error al procesar el JSON (archivo corrupto)
            // TODO: Manejar la excepción apropiadamente
            e.printStackTrace();
        } catch (IOException e) {
            // Error al leer o escribir el archivo de configuración
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

    public void addEntryAndSave(VaultEntry entry) {
        try {
            if (currenVault == null || masterKey == null || encryptedMasterKey == null) {
                throw new IllegalStateException("No vault is currently loaded or created.");
            }
    
            currenVault.addEntry(entry);
    
            String data = jsonService.toJson(currenVault.getEntries());
    
            byte[] encryptedData = cipherService.encrypt(data.getBytes(), masterKey, currenVault.getDataIV());
    
            VaultFile vaultFile = new VaultFile(
                currenVault.getSalt(),
                currenVault.getKeyIV(),
                currenVault.getDataIV(),
                encryptedMasterKey,
                encryptedData
            );
    
            vaultFileIOService.writeVaultFile(currentVaultPath, vaultFile);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void updateEntryAndSave(VaultEntry oldEntry, VaultEntry newEntry) {
        try {
            if (currenVault == null || masterKey == null || encryptedMasterKey == null) {
                throw new IllegalStateException("No vault is currently loaded or created.");
            }

            int entryIndex = currenVault.getEntries().indexOf(oldEntry);

            if (entryIndex == -1) {
                throw new IllegalStateException("The entry to be updated was not found in the current vault.");
            }

            currenVault.getEntries().set(entryIndex, newEntry);

            String data = jsonService.toJson(currenVault.getEntries());

            byte[] encryptedData = cipherService.encrypt(data.getBytes(), masterKey, currenVault.getDataIV());

            VaultFile vaultFile = new VaultFile(
                currenVault.getSalt(),
                currenVault.getKeyIV(),
                currenVault.getDataIV(),
                encryptedMasterKey,
                encryptedData
            );

            vaultFileIOService.writeVaultFile(currentVaultPath, vaultFile);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void deleteEntryAndSave(VaultEntry entryToRemove) {
        try {
            if (currenVault == null || masterKey == null || encryptedMasterKey == null) {
                throw new IllegalStateException("No vault is currently loaded or created.");
            }

            boolean removed = currenVault.getEntries().remove(entryToRemove);

            if (!removed) {
                throw new IllegalStateException("The entry to be deleted was not found in the current vault.");
            }

            String data = jsonService.toJson(currenVault.getEntries());

            byte[] encryptedData = cipherService.encrypt(data.getBytes(), masterKey, currenVault.getDataIV());

            VaultFile vaultFile = new VaultFile(
                currenVault.getSalt(),
                currenVault.getKeyIV(),
                currenVault.getDataIV(),
                encryptedMasterKey,
                encryptedData
            );

            vaultFileIOService.writeVaultFile(currentVaultPath, vaultFile);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static Vault getCurrentVault() {
        return currenVault;
    }

    public static String getCurrentVaultFileName() {
        return currentVaultFileName;
    }

    public static Path getCurrentVaultPath() {
        return currentVaultPath;
    }

    public void changeMasterPassword(String oldPassword, String newPassword) throws DecryptionException {
        if (currenVault == null || masterKey == null || encryptedMasterKey == null) {
            throw new IllegalStateException("No vault is currently loaded or created.");
        }

        try {
            byte[] oldDerivedKey = cipherService.deriveKey(oldPassword, currenVault.getSalt());
            byte[] decryptedMasterKey = cipherService.decrypt(encryptedMasterKey, oldDerivedKey, currenVault.getKeyIV());

            byte[] newDerivedKey = cipherService.deriveKey(newPassword, currenVault.getSalt());

            byte[] newEncryptedMasterKey = cipherService.encrypt(decryptedMasterKey, newDerivedKey, currenVault.getKeyIV());

            VaultManagementService.encryptedMasterKey = newEncryptedMasterKey;

            VaultFile vaultFile = new VaultFile(
                currenVault.getSalt(),
                currenVault.getKeyIV(),
                currenVault.getDataIV(),
                newEncryptedMasterKey,
                cipherService.encrypt(jsonService.toJson(currenVault.getEntries()).getBytes(), masterKey, currenVault.getDataIV())
            );
            vaultFileIOService.writeVaultFile(currentVaultPath, vaultFile);

        } catch (DecryptionException e) {
            throw e;
        } catch (CipherException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error de cifrado al cambiar la contraseña maestra: " + e.getMessage(), e);
        } catch (EncryptionException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error de encriptación al cambiar la contraseña maestra: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error de procesamiento JSON al cambiar la contraseña maestra: " + e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error de E/S al cambiar la contraseña maestra: " + e.getMessage(), e);
        }
    }


}
