package com.example.dam_proyecto_final.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.ui.home.HomeActivity;
import com.example.dam_proyecto_final.ui.walktrough.WalktroughActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
                boolean walktroughtDone = preferencias.getBoolean("walktroughtDone", false);
                String userEmail = preferencias.getString("email", null);

                Intent intent;
                if (!walktroughtDone) {
                    intent = new Intent(getApplicationContext(), WalktroughActivity.class);
                } else if (userEmail != null) {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
                startActivity(intent);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 400); // 4000

    }
}