package com.example.dam_proyecto_final.model;

public class MemberModel {

    private String email;
    private int role;

    public MemberModel(String email, int role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
