package com.example.dam_proyecto_final;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class SavedData extends AppCompatActivity {




    public SavedData(){}



    public SharedPreferences getPreferencias() {

        SharedPreferences preferencias = super.getSharedPreferences("savedData", super.getApplicationContext().MODE_PRIVATE);
        return preferencias;
    }
}
