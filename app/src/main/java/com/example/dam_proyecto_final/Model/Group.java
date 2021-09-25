package com.example.dam_proyecto_final.Model;

public class Group {

    private int id;
    private String nombre;
    private String descripción;
    //TODO Meter permisos a esta parte para controlar que puede hacer en cada uno cuando lo seleccione
    public Group(int id, String nombre, String descripción) {
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
