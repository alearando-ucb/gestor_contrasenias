package com.ucb.amae.vault.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationsService {

    private static final String CONFIG_FILE = "config.properties";
    public static final String LAST_VAULT_PATH_KEY = "last_vault_path";
    public static final String LAST_VAULT_NAME_KEY = "last_vault_name";

    public void saveLastVault(String path, String name) throws IOException {
        Properties props = new Properties();
        props.setProperty(LAST_VAULT_PATH_KEY, path);
        props.setProperty(LAST_VAULT_NAME_KEY, name);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Last Opened Vault Configuration");
        }
    }

    public Properties loadLastVault() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        }
        return props;
    }
}
