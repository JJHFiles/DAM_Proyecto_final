package com.example.dam_proyecto_final.Model;

public class JsonResponse {

    private int id;
    private String message;

    public JsonResponse(int id, String message) {
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
