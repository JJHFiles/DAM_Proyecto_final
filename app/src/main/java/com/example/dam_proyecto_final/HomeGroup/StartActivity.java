package com.example.dam_proyecto_final.HomeGroup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.dam_proyecto_final.HomeGroup.ui.SectionsPagerAdapter;
import com.example.dam_proyecto_final.databinding.ActivityStartBinding;
import com.google.android.material.tabs.TabLayout;

public class StartActivity extends AppCompatActivity {

    private String email = "", pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/* Para en caso de recibir un putExtra
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String email = bundle.getString("email");
            String pass = bundle.getString("pass");
        }
*/
        ActivityStartBinding binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }
}