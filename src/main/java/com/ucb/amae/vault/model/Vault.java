package com.ucb.amae.vault.model;

import java.util.ArrayList;
import java.util.List;

public class Vault {

    private byte[] salt;
    private byte[] keyIV;
    private byte[] dataIV;
    private List<VaultEntry> entries;

    public Vault() {
        this.entries = new ArrayList<>();
    }
    
    public Vault(byte[] salt, byte[] keyIV, byte[] dataIV, List<VaultEntry> entries) {
        this.salt = salt;
        this.keyIV = keyIV;
        this.dataIV = dataIV;
        this.entries = entries;
    }

    public Vault(byte[] salt, byte[] keyIV, byte[] dataIV) {
        this.salt = salt;
        this.keyIV = keyIV;
        this.dataIV = dataIV;
        this.entries = new ArrayList<>();
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

    public List<VaultEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<VaultEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(VaultEntry entry) {
        this.entries.add(entry);
    }

    public void removeEntry(VaultEntry entry) {
        this.entries.remove(entry);
    }

}
