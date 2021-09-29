package com.example.dam_proyecto_final.Model;

import java.sql.Date;

public class ActivityModel {

// BD activity table fields:  idactivity, date_activity(type datetime), action, idgroup, email

    private String idactivity;
    private String action;
    private String date_activity;
    private String name;
    private String description;
    private String email;


    public ActivityModel(String idactivity,
                         String action,
                         String date_activity,
                         String name,
                         String description,
                         String email
                         ) {
        this.idactivity = idactivity;
        this.action = action;
        this.date_activity = date_activity;
        this.description = description;
        this.email = email;
        this.name = name;
    }

    public String getIdactivity() {
        return idactivity;
    }

    public String getAction() {
        return action;
    }

    public String getDate_activity() {
        return date_activity;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
