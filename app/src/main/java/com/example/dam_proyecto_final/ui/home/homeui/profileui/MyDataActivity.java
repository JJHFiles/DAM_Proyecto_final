package com.example.dam_proyecto_final.ui.home.homeui.profileui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.dam_proyecto_final.R;

import java.util.Objects;

public class MyDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);

        this.setTitle(getString(R.string.my_data));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        EditText et_mydata_name = findViewById(R.id.et_mydata_name);
        EditText et_mydata_email = findViewById(R.id.et_mydata_email);

        String userEmail = getIntent().getExtras().getString("userEmail", "");
        String name = getIntent().getExtras().getString("name", "");

        et_mydata_name.setText(name);
        et_mydata_email.setText(userEmail);

    }
}