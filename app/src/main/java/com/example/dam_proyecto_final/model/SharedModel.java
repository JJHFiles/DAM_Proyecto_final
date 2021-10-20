package com.example.dam_proyecto_final.model;

public class SharedModel {

    private String email;
    private int idgroup;
    private int role;

    public SharedModel(String email, int idgroup, int role) {
        this.email = email;
        this.idgroup = idgroup;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public int getIdgroup() {
        return idgroup;
    }

    public int getRole() {
        return role;
    }
}
