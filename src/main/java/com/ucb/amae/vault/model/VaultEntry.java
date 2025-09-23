package com.ucb.amae.vault.model;

public class VaultEntry {

    private String serviceName;
    private String username;
    private String password;
    private String url;

    public VaultEntry() {
    }

    public VaultEntry(String serviceName, String username, String password, String url) {
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public VaultEntry(String serviceName, String username, String password) {
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VaultEntry{" +
                "serviceName='" + serviceName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VaultEntry that = (VaultEntry) o;

        if (!serviceName.equals(that.serviceName)) return false;
        if (!username.equals(that.username)) return false;
        if (!password.equals(that.password)) return false;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() { 
        int result = serviceName.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }   
}
