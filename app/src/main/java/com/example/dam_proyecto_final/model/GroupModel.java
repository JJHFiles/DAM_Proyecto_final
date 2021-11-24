package com.example.dam_proyecto_final.model;

import java.io.Serializable;

public class GroupModel implements Serializable {

    private int id;
    private String name;
    private String description;
    private String currency;
    private int role;

    //TODO Meter permisos a esta parte para controlar que puede hacer en cada uno cuando lo seleccione
    public GroupModel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public GroupModel(int id, String name, String description, String currency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currency = currency;

    }
    public GroupModel(int id, String name, String description, String currency, int role) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.role = role;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }

    public int getRole() {
        return role;
    }

}
