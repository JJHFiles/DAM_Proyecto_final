package com.example.dam_proyecto_final.ui.walktrough;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.dam_proyecto_final.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WalktroughActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walktrough);

        ViewPager2 vp2Walktrough = findViewById(R.id.vp2_aw_walktrough);
        TabLayout tabDots = findViewById(R.id.tab_fw_dots);


        vp2Walktrough.setAdapter(new WalktroughStateAdapter(getSupportFragmentManager(), getLifecycle()));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabDots, vp2Walktrough,
                true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) { }
        });
        tabLayoutMediator.attach();

    }
}