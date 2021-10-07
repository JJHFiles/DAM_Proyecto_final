package com.example.dam_proyecto_final.home.homeui.group_invoice;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceTabChartFragment;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceTabListFragment;
import com.example.dam_proyecto_final.R;
import com.google.android.material.tabs.TabLayout;

//https://material.io/components/tabs/android#fixed-tabs

public class GroupInvoiceTab extends AppCompatActivity {
    private String userEmail, idGroup, groupName, currency, role;

    private TabLayout tabLayout;
    private ImageView iv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_tab);


        tabLayout = findViewById(R.id.tabLayout);
        iv = findViewById(R.id.iv);


        this.setTitle(groupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Por defecto listado
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fcv_AGITListChart, GroupInvoiceTabListFragment.class, null)
                .commit();


        // tab1 index -> tab.getPosition()==0, tab2 -> 1
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        Toast.makeText(getApplicationContext(), "TAB: " + tab.getPosition(), Toast.LENGTH_LONG).show();
                        if (tab.getPosition() == 0) {

                            fragmentManager.beginTransaction()
                                    .replace(R.id.fcv_AGITListChart, GroupInvoiceTabListFragment.class, null)
                                    .addToBackStack(null) // name can be null
                                    .commit();

                        }
                        if (tab.getPosition() == 1) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fcv_AGITListChart, GroupInvoiceTabChartFragment.class, null)
                                    .addToBackStack(null) // name can be null
                                    .commit();

                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        // ...
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        // ...
                    }
                }
        );


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_invoice_home_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnu_GIHAFilter:
                //TODO crear flujo de filtro
                return true;
            case R.id.mnu_GIHAEditGroup:
                //TODO crear flujo de grupo
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}