package com.example.dam_proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dam_proyecto_final.home.HomeActivity;
import com.example.dam_proyecto_final.walktrough.WalktroughActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO SplashScreen

        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        Boolean walktroughtDone = preferencias.getBoolean("walktroughtDone", false);
        String userEmail = preferencias.getString("email", null);

        Intent intent;
        if (!walktroughtDone){
            intent = new Intent(getApplicationContext(), WalktroughActivity.class);
        } else if(userEmail != null) {
            intent = new Intent(getApplicationContext(), HomeActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        startActivity(intent);

    }
}