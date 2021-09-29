package com.example.dam_proyecto_final.Model;

public class JsonResponseModel {

    private int id;
    private String message;

    public JsonResponseModel(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

}
