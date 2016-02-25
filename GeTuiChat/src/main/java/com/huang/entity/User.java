package com.huang.entity;

/**
 * Created by huang on 12/26/15.
 */
public class User {

    private String username;
    private String password;
    private String clientid;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", clientid='" + clientid + '\'' +
                '}';
    }

    public User() {

    }

    public User(String username, String password, String clientid) {
        this.username = username;
        this.password = password;
        this.clientid = clientid;
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

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }
}
