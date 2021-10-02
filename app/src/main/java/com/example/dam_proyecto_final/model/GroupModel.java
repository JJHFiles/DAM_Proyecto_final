package com.example.dam_proyecto_final.model;

public class GroupModel {

    private int id;
    private String nombre;
    private String descripción;
    //TODO Meter permisos a esta parte para controlar que puede hacer en cada uno cuando lo seleccione
    public GroupModel(int id, String nombre, String descripción) {
        this.id = id;
        this.nombre = nombre;
        this.descripción = descripción;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripción() {
        return descripción;
    }

}
