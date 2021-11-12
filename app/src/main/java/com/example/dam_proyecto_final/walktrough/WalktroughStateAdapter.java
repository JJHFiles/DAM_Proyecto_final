package com.example.dam_proyecto_final.walktrough;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WalktroughStateAdapter extends FragmentStateAdapter {

    public WalktroughStateAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = WalktroughFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
