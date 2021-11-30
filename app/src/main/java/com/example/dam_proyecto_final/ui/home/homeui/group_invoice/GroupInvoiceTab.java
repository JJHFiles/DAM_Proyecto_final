package com.example.dam_proyecto_final.ui.home.homeui.group_invoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.ui.home.HomeActivity;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceFilter;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceTabChartFragment;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceTabListFragment;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui.edit_group.GroupInvoiceEditGroup;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class GroupInvoiceTab extends AppCompatActivity {

    private GroupModel groupModel;
    private String userEmail;
    private String userPass;

    private TabLayout tabLayout;
    private ArrayList<InvoiceModel> arrIM;
    private ArrayList<InvoiceModel> arrIMFilter;

    ActivityResultLauncher<Intent> intentForResult;
    WebApiRequest webApiRequest;
    //Filter parameters
    ArrayList<String> cbSelectedFilter;
    int typeChart;
    Calendar dateFromFilter;
    Calendar dateTofilter;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_tab);

        tabLayout = findViewById(R.id.tabLayout);

        //Instanciamos el FragmentManager para la implementación de las vistas
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Cogemos la información de grupo obtenida del GroupFragment
        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            groupModel = (GroupModel) parametros.getSerializable("groupModel");
            userEmail = parametros.getString("userEmail", "vacio");
            userPass = parametros.getString("userPass", "vacio");

            Log.d("DEBUGME", "GroupInvoiceTab: group " + groupModel.getId());

        } else {
            Log.d("DEBUGME", "GroupInvoiceTab: FATAL ERROR idGroup = null");
        }



        //Establecemos titulo al banner y flecha de back
        this.setTitle(groupModel.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializamos parametros de filtro
        cbSelectedFilter = null;
        typeChart = -1;
        dateFromFilter = null;
        dateTofilter = null;


        //Obtenemos la lista de facturas que pasaremos a los fragment, al ser la carga inicial tambien invocamos al fragment de lista principal
        webApiRequest = new WebApiRequest(this);
        webApiRequest.getInvoiceByGroup(userEmail, userPass, groupModel.getId(), new WebApiRequest.WebApiRequestJsonObjectArrayListener() {
            @Override
            public void onSuccess(JsonResponseModel response, List<?> data) {
                Log.d("DEBUGME", "GroupInvoiceTab: " + response.getId() + " " + response.getMessage());

                if (response.getId() == 252) {
                    Intent intent = new Intent(getApplicationContext(), GroupInvoiceEmptyActivity.class);
                    intent.putExtra("groupModel", groupModel);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userPass", userPass);
                    finish();
                    startActivity(intent);

                } else {

                    //Inicializamos la lista de facturas
                    arrIM = (ArrayList<InvoiceModel>) data;

                    //Si obtenemos la lista invocamos al fragment
                    GroupInvoiceTabListFragment fragment = GroupInvoiceTabListFragment.newInstance(
                            groupModel, userEmail, userPass, arrIM
                    );
                    fragmentManager.beginTransaction()
                            .replace(R.id.fcv_AGITListChart, fragment, null)
                            .commit();
                }

            }

            @Override
            public void onError(JsonResponseModel response) {

                Log.d("DEBUGME", "GroupInvoiceTab: " + response.getId() + " " + response.getMessage());
            }
        });


        //Método de selección del fragment al cambiar de selección en la tab
        // tab1 index -> tab.getPosition()==0, tab2 -> 1
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        //Toast.makeText(getApplicationContext(), "TAB: " + tab.getPosition(), Toast.LENGTH_LONG).show();
                        Log.d("DEBUGME", "TAB: " + tab.getPosition());

                        replaceFragment(tab.getPosition(), fragmentManager);

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

        // Método de captura del resultado de la pantalla filtro
        intentForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = result.getData().getExtras();

                            // Obtenemos los parametros de filtro
                            arrIMFilter = bundle.getParcelableArrayList("arrIMFilter");

                            cbSelectedFilter = bundle.getStringArrayList("cbSelected");
                            typeChart = bundle.getInt("typeChart");
                            dateFromFilter = (Calendar) bundle.getSerializable("dateFrom");
                            dateTofilter = (Calendar) bundle.getSerializable("dateTo");


                            // Refrescamos el fragment previamente seleccionado con los nuevos parametros
                            TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                            replaceFragment(tab.getPosition(), fragmentManager);
                        }
                    }
                });
    }

    //Método de gestión para el cambio de fragmento
    private void replaceFragment(int position, FragmentManager fragmentManager) {
        // Elegimos la lista a pasar como parametro priorizando la filtrada
        Bundle bundle = new Bundle();
        ArrayList<InvoiceModel> invoices;
        if (arrIMFilter != null) {
//            bundle.putParcelableArrayList("invoices", arrIMFilter);
            invoices = arrIMFilter;
        } else {
//            bundle.putParcelableArrayList("invoices", arrIM);
            invoices = arrIM;
        }

        if (position == 0) {
//            bundle.putString("currency", groupModel.getCurrency());
            GroupInvoiceTabListFragment fragment = GroupInvoiceTabListFragment.newInstance(
                    groupModel, userEmail, userPass, invoices
            );
            fragmentManager.beginTransaction()
                    .replace(R.id.fcv_AGITListChart, fragment, null)
                    .commit();
        }

        if (position == 1) {
            bundle.putInt("typeChart", typeChart);
            bundle.putParcelableArrayList("invoices", invoices);

            GroupInvoiceTabChartFragment fragment = new GroupInvoiceTabChartFragment();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.fcv_AGITListChart, fragment, null)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_invoice_home_activity_menu, menu);
        this.menu = menu;
        applyPermission();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.mnu_GIHAFilter:
                Intent intentFilter = new Intent(this, GroupInvoiceFilter.class);
                intentFilter.putExtra("invoices", arrIM);
                intentFilter.putExtra("currency", groupModel.getCurrency());
                if (cbSelectedFilter != null) {
                    intentFilter.putExtra("cbSelected", cbSelectedFilter);
                }
                if (typeChart != -1) {
                    intentFilter.putExtra("typeChart", typeChart);
                }
                if (dateFromFilter != null) {
                    intentFilter.putExtra("dateFrom", dateFromFilter);
                }
                if (dateTofilter != null) {
                    intentFilter.putExtra("dateTo", dateTofilter);
                }
                intentForResult.launch(intentFilter);
                return true;

            case R.id.mnu_GIHAEditGroup:
                Intent intent = new Intent(getApplicationContext(), GroupInvoiceEditGroup.class);
                intent.putExtra("idGroup", groupModel.getId());
                intent.putExtra("groupName", groupModel.getName());
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userPass", userPass);
                intent.putExtra("currency", groupModel.getCurrency());
                intent.putExtra("groupRole", groupModel.getRole());

                startActivity(intent);
                return true;

            case R.id.mnu_GIHAExitGroup:
                webApiRequest.exitGroup(userEmail, userPass, groupModel.getId(), groupModel.getName(), new WebApiRequest.WebApiRequestJsonResponseListener() {
                    @Override
                    public void onSuccess(JsonResponseModel response) {
                        Toast.makeText(getApplicationContext(), getString(R.string.warning_exit_group), Toast.LENGTH_LONG).show();

                        //Volvemos al login activity
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(JsonResponseModel response) {
                        Toast.makeText(getApplicationContext(), getString(R.string.warning_exitgroup_error), Toast.LENGTH_LONG).show();

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // Do what you need done here
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_rigth, R.anim.right_to_left);

        this.finish();
    }

    public void applyPermission(){
        if (this.groupModel.getRole()>=2) {
            MenuItem item = menu.findItem(R.id.mnu_GIHAEditGroup);
            item.setVisible(false);
        } else {
            MenuItem item = menu.findItem(R.id.mnu_GIHAExitGroup);
            item.setVisible(false);
        }
    }

}