package com.example.dam_proyecto_final.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.home.homeui.ActivityFragment;
import com.example.dam_proyecto_final.home.homeui.GroupFragment;
import com.example.dam_proyecto_final.home.homeui.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends FragmentActivity implements NavigationBarView.OnItemSelectedListener {

    static private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Lectura de datos drecibidos desde el el login
        bottom_navigation = findViewById(R.id.bttmNav);
        bottom_navigation.setOnItemSelectedListener(this);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fcvHome, GroupFragment.class, null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.page_group:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvHome, GroupFragment.class, null)
                        .addToBackStack(null) // name can be null
                        .commit();
                return true;
            case R.id.page_activity:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvHome, ActivityFragment.class, null)
                        .addToBackStack(null) // name can be null
                        .commit();
                return true;
            case R.id.page_profile:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvHome, ProfileFragment.class, null)
                        .addToBackStack(null) // name can be null
                        .commit();
                return true;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvHome, GroupFragment.class, null)
                        .addToBackStack(null) // name can be null
                        .commit();
                return true;
        }
    }
}