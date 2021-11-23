package com.example.dam_proyecto_final.ui.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.ui.home.homeui.ActivityFragment;
import com.example.dam_proyecto_final.ui.home.homeui.GroupFragment;
import com.example.dam_proyecto_final.ui.home.homeui.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends FragmentActivity implements NavigationBarView.OnItemSelectedListener {

    static private BottomNavigationView bottom_navigation;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Lectura de datos drecibidos desde el el login
        bottom_navigation = findViewById(R.id.bttmNav);
        bottom_navigation.setOnItemSelectedListener(this);


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fcvHome, GroupFragment.class, null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.page_group:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvHome, GroupFragment.class, null)
                        .commit();
                return true;
            case R.id.page_activity:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvHome, ActivityFragment.class, null)
                        .commit();
                return true;
            case R.id.page_profile:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvHome, ProfileFragment.class, null)
                        .commit();
                return true;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.fcvHome, GroupFragment.class, null)
                        .commit();
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (bottom_navigation.getSelectedItemId() == R.id.page_group){
            finish();
        } else {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fcvHome, GroupFragment.class, null)
                    .commit();
            bottom_navigation.setSelectedItemId(R.id.page_group);
        }
    }
}