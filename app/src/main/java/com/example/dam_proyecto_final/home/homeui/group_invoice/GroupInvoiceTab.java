package com.example.dam_proyecto_final.home.homeui.group_invoice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.dam_proyecto_final.home.HomeActivity;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceTabChartFragment;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.GroupInvoiceTabListFragment;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.edit_group.GroupInvoiceEditGroup;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

//https://material.io/components/tabs/android#fixed-tabs

public class GroupInvoiceTab extends AppCompatActivity {
    private String userEmail, idGroup, groupName, currency, role,userPass;

    private TabLayout tabLayout;
    private ImageView iv;
    private ArrayList<InvoiceModel> arrIM;


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
            idGroup = parametros.getString("idGroup", "vacio");
            groupName = parametros.getString("groupName", "vacio");
            userEmail = parametros.getString("userEmail", "vacio");
            userPass = parametros.getString("userPass", "vacio");
            currency = parametros.getString("currency", "vacioCurrency");
            role = parametros.getString("role", "vacioRole");

//            Toast.makeText(context, "idGroup " + idGroup, Toast.LENGTH_LONG).show();
            Log.d("DEBUGME", "GroupInvoiceTab: grupo " + idGroup);

        } else {
            Log.d("DEBUGME", "GroupInvoiceTab: ERROR GRAVE idGroup = null");
//            Toast.makeText(context, "ERROR GRAVE idGroup = null", Toast.LENGTH_LONG).show();
        }

        //Establecemos titulo al banner y flecha de back
        this.setTitle(groupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        Toast.makeText(getApplicationContext(), "TAB: " + tab.getPosition(), Toast.LENGTH_LONG).show();
                        if (tab.getPosition() == 0) {

                            GroupInvoiceTabListFragment fragment = new GroupInvoiceTabListFragment();
                            //Creamos el bundle y pasamos las facturas
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("invoices", arrIM);
                            bundle.putString("currency", currency);
                            fragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fcv_AGITListChart, fragment, null)
                                    .addToBackStack(null) // name can be null
                                    .commit();

                        }
                        if (tab.getPosition() == 1) {

                            GroupInvoiceTabChartFragment fragment = new GroupInvoiceTabChartFragment();
                            //Creamos el bundle y asignamos
                            //Creamos el bundle y pasamos las facturas
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("invoices", arrIM);
                            bundle.putString("currency", currency);
                            fragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fcv_AGITListChart, fragment, null)
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