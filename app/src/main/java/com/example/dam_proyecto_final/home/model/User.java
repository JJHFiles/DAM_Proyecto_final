package com.example.dam_proyecto_final.home.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private String email;
    private String password;
    private String name;
    private Date date_signup;

    //Constructor usado para el inicio por google
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    //Constructor usado por el método de registro
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
