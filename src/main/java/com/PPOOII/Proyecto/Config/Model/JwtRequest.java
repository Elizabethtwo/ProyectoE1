package com.PPOOII.Proyecto.Config.Model;

import java.io.Serializable;

public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;

    // Constructors
    public JwtRequest() {}

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
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
}
