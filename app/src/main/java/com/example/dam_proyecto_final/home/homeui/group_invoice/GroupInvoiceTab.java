package com.example.dam_proyecto_final.home.homeui.group_invoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceFilter;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceTabChartFragment;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceTabListFragment;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.edit_group.GroupInvoiceEditGroup;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//https://material.io/components/tabs/android#fixed-tabs

public class GroupInvoiceTab extends AppCompatActivity {


    private String userEmail;
    private int idGroup;
    private String groupName;
    private String currency;
    private String role;
    private String userPass;

    private TabLayout tabLayout;
    private ImageView iv;
    private ArrayList<InvoiceModel> arrIM;
    private ArrayList<InvoiceModel> arrIMFilter;

    ActivityResultLauncher<Intent> intentForResult;

    //Filter parameters
    ArrayList<String> cbSelectedFilter;
    int typeChart;
    Calendar dateFromFilter;
    Calendar dateTofilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_tab);


        tabLayout = findViewById(R.id.tabLayout);
        iv = findViewById(R.id.iv);

        //Instanciamos el FragmentManager para la implementación de las vistas
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Cogemos la información de grupo obtenida del GroupFragment
        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            userEmail = parametros.getString("userEmail", "vacio");
            GroupModel group = (GroupModel) parametros.getSerializable("group");
            idGroup = group.getId();
            groupName = group.getName();
            currency = group.getCurrency();
            role = group.getRole();
            userPass = parametros.getString("userPass", "vacio");
            role = parametros.getString("role", "vacioRole");


            Log.d("DEBUGME", "GroupInvoiceTab: grupo " + idGroup);

        } else {
            Log.d("DEBUGME", "GroupInvoiceTab: ERROR GRAVE idGroup = null");
        }

        //Establecemos titulo al banner y flecha de back
        this.setTitle(groupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializamos parametros de filtro
        cbSelectedFilter = null;
        typeChart = -1;
        dateFromFilter = null;
        dateTofilter = null;

        //Obtenemos la lista de facturas que pasaremos a los fragment, al ser la carga inicial tambien invocamos al fragment de lista principal
        WebApiRequest webApiRequest = new WebApiRequest(this);
        webApiRequest.getInvoiceByGroup(idGroup, new WebApiRequest.WebApiRequestJsonObjectArrayListener() {
            @Override
            public void onSuccess(JsonResponseModel response, List<?> data) {
                Log.d("DEBUGME", "GroupInvoiceTab: " + response.getId() + " " + response.getMessage());

                //Inicializamos la lista de facturas
                arrIM = (ArrayList<InvoiceModel>) data;

                //Si obtenemos la lista invocamos al fragment
                GroupInvoiceTabListFragment fragment = new GroupInvoiceTabListFragment();

                //Creamos el bundle y asignamos
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("invoices", arrIM);
                bundle.putString("currency", currency);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fcv_AGITListChart, fragment, null)
                        .addToBackStack(null) // name can be null
                        .commit();
            }

            @Override
            public void onError(JsonResponseModel response) {
                //En caso de error al obtener las facturas lo indicamos al usaurio
                if (response.getId() == -253) {
                    //Si es -252 es que el usuario no tiene actividades
                    Toast.makeText(getApplicationContext(), "Error " + response.getId(), Toast.LENGTH_LONG).show();
                    Log.d("DEBUGME", "GroupInvoiceTab: " + response.getId() + " " + response.getMessage());
                } else {
                    //Si no ha podido ser cualquier error
                    Toast.makeText(getApplicationContext(), "Error al obtener facturas" + response.getId(), Toast.LENGTH_LONG).show();
                    Log.d("DEBUGME", "GroupInvoiceTab: " + response.getId() + " " + response.getMessage());
                }
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

        // Register
        intentForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = result.getData().getExtras();

                            // Obtenemos los parametros de filtro
                            arrIMFilter = bundle.getParcelableArrayList("arrIMFilter");

                            cbSelectedFilter  = bundle.getStringArrayList("cbSelected");
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

    private void replaceFragment(int position, FragmentManager fragmentManager) {
        // Elegimos la lista a pasar como parametro priorizando la filtrada
        Bundle bundle = new Bundle();
        if (arrIMFilter != null){
            bundle.putParcelableArrayList("invoices", arrIMFilter);
        } else {
            bundle.putParcelableArrayList("invoices", arrIM);
        }

        if (position == 0) {
            bundle.putString("currency", currency);
            GroupInvoiceTabListFragment fragment = new GroupInvoiceTabListFragment();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.fcv_AGITListChart, fragment, null)
                    .addToBackStack(null) // name can be null
                    .commit();
        }

        if (position == 1) {
            bundle.putInt("typeChart", typeChart);
            GroupInvoiceTabChartFragment fragment = new GroupInvoiceTabChartFragment();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.fcv_AGITListChart, fragment, null)
                    .addToBackStack(null) // name can be null
                    .commit();
        }
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
                Intent intentFilter = new Intent(this, GroupInvoiceFilter.class);
                intentFilter.putExtra("invoices", arrIM);
                intentFilter.putExtra("currency", currency);
                if (cbSelectedFilter != null){
                    intentFilter.putExtra("cbSelected", cbSelectedFilter);
                }
                if (typeChart != -1){
                    intentFilter.putExtra("typeChart", typeChart);
                }
                if (dateFromFilter != null){
                    intentFilter.putExtra("dateFrom", dateFromFilter);
                }
                if (dateTofilter != null){
                    intentFilter.putExtra("dateTo", dateTofilter);
                }
                intentForResult.launch(intentFilter);
                return true;
            case R.id.mnu_GIHAEditGroup:
                //TODO crear flujo de grupo

                Intent intent = new Intent(getApplicationContext(), GroupInvoiceEditGroup.class);
                intent.putExtra("idGroup", idGroup);
                intent.putExtra("groupName", groupName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userPass", userPass);
                intent.putExtra("currency", currency);
                startActivity(intent);




                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}