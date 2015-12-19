package com.ryblade.openbikebcn.Model;

/**
 * Created by alexmorral on 19/12/15.
 */
public class User {


    private int userId;
    private String email;
    private String username;


    public User(int userId, String email, String username) {
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
