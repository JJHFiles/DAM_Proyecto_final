package com.example.dam_proyecto_final.model;

public class ActivityModel {

// BD activity table fields:  idactivity, date_activity(type datetime), action, idgroup, email

    private int idactivity;
    private String date_activity;
    private String action;
    private int idgroup;
    private String email;
    private int icon;


    public ActivityModel(int idactivity, String date_activity, String action, int idgroup, String email, int icon) {
        this.idactivity = idactivity;
        this.date_activity = date_activity;
        this.action = action;
        this.idgroup = idgroup;
        this.email = email;
        this.icon = icon;
    }

    public int getIdactivity() {
        return idactivity;
    }

    public String getDate_activity() {
        return date_activity;
    }

    public String getAction() {
        return action;
    }

    public int getIdgroup() {
        return idgroup;
    }

    public String getEmail() {
        return email;
    }

    public int getIcon() {
        return icon;
    }


}
