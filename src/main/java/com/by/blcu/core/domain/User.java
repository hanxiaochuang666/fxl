package com.by.blcu.core.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 3918296410620195600L;

    private String username;

    private String password;

    private Set<String> role;

    private Set<String> permission;

    public User(String username, String password, Set<String> role, Set<String> permission) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.permission = permission;
    }


}
