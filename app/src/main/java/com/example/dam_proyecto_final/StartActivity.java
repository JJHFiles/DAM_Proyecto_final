package com.example.dam_proyecto_final;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.dam_proyecto_final.homeui.ActivityFragment;
import com.example.dam_proyecto_final.homeui.GroupFragment;
import com.example.dam_proyecto_final.homeui.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class StartActivity extends FragmentActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnItemSelectedListener(this);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fcvhome, GroupFragment.class, null)
                .addToBackStack(null) // name can be null
                .commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.page_group:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvhome, GroupFragment.class, null)
                        .addToBackStack(null) // name can be null
                        .commit();
                return true;
            case R.id.page_activity:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvhome, ActivityFragment.class, null)
                        .addToBackStack(null) // name can be null
                        .commit();
                return true;
            case R.id.page_profile:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvhome, ProfileFragment.class, null)
                        .addToBackStack(null) // name can be null
                        .commit();
                return true;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvhome, GroupFragment.class, null)
                        .addToBackStack(null) // name can be null
                        .commit();
                return true;
        }
    }
}