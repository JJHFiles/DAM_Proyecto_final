package com.example.dam_proyecto_final.Model;

public class Group {

    private String nombre;
    private String descripción;
    //TODO Meter permisos a esta parte para controlar que puede hacer en cada uno cuando lo seleccione

    public Group(String nombre, String descripción) {
        this.nombre = nombre;
        this.descripción = descripción;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripción() {
        return descripción;
    }

}
