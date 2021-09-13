package com.example.dam_proyecto_final.home.model;

import java.util.Date;

public class User {

    private String email;
    private String password;
    private String name;
    private Date date_signup;

    //Constructor usado para el inicio por google
    public User(String email, String name, Date date_signup) {
        this.email = email;
        this.name = name;
        this.date_signup = date_signup;
    }

    //Constructor usado por el método de registro
    public User(String email, String password, String name, Date date_signup) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.date_signup = date_signup;
    }
}
