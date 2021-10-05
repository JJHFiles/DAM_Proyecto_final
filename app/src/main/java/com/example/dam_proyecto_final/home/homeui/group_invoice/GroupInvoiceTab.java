package com.example.dam_proyecto_final.home.homeui.group_invoice;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.InvoiceListAdapter;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.InvoiceListModel;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

//https://material.io/components/tabs/android#fixed-tabs

public class GroupInvoiceTab extends AppCompatActivity implements View.OnClickListener {
    private String userEmail, idGroup, groupName,currency,role;
    private WebApiRequest webApiRequest;
    private ListView lv_invoice;
    private TabLayout tabLayout;
    private ImageView iv;
    private ImageButton ibAdd;

    private ExtendedFloatingActionButton btOCR,btManual;

    private ArrayList<InvoiceModel> arrIM;
    private ArrayList<InvoiceListModel> ilm=new ArrayList<InvoiceListModel>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_tab);

        ibAdd = findViewById(R.id.ibAdd);
        ibAdd.setOnClickListener(this);
        btManual = findViewById(R.id.btManual);
        btOCR = findViewById(R.id.btOCR);
        btManual.setOnClickListener(this);
        btOCR.setOnClickListener(this);


        lv_invoice = findViewById(R.id.lv_invoice);
        tabLayout = findViewById(R.id.tabLayout);
        iv = findViewById(R.id.iv);



        Bundle parametros = this.getIntent().getExtras();

        if (parametros != null) {
            idGroup = parametros.getString("idGroup", "vacio");
            groupName = parametros.getString("groupName", "vacio");
            userEmail = parametros.getString("userEmail", "vacio");
            currency=parametros.getString("currency", "vacioCurrency");
            role=parametros.getString("role", "vacioRole");


            Toast.makeText(getApplicationContext(), "idGroup " + idGroup, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "ERROR GRAVE idGroup = null", Toast.LENGTH_LONG).show();
        }

        this.setTitle(groupName);


        // tab1 index -> tab.getPosition()==0, tab2 -> 1
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        Toast.makeText(getApplicationContext(), "TAB: " + tab.getPosition(), Toast.LENGTH_LONG).show();
                        if (tab.getPosition() == 0) {
                            iv.setVisibility(View.INVISIBLE);
                            lv_invoice.setVisibility(View.VISIBLE);
                            fillListViewCustomAdapter();

                        }
                        if (tab.getPosition() == 1) {
                            iv.setVisibility(View.VISIBLE);
                            lv_invoice.setVisibility(View.INVISIBLE);
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
        getInvoiceByGroup(idGroup);
    }


    // recibe las facturas en ese grupo seleccionado
    public void getInvoiceByGroup(String idGroup) {
        webApiRequest = new WebApiRequest(getApplicationContext());
        webApiRequest.getInvoiceByGroup(idGroup, new WebApiRequest.WebApiRequestJsonObjectArrayListener() {
            @Override
            public void onSuccess(JsonResponseModel response, List<?> data) {
                Log.d("DEBUGME", response.getId() + " " + response.getMessage());

                arrIM = (ArrayList<InvoiceModel>) data;

                // fillListViewSimpleAdapter(arrIM);
                fillListViewCustomAdapter();


            }

            @Override
            public void onError(JsonResponseModel response) {
                if (response.getId() == -253) {
                    //Si es -252 es que el usuario no tiene actividades
                    //Toast.makeText(getApplicationContext(), "Error " + response.getId() + " ?", Toast.LENGTH_LONG).show();


                } else {
                    //Si no ha podido ser cualquier error
                    //Toast.makeText(getApplicationContext(), "Error " + response.getId(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void fillListViewSimpleAdapter(ArrayList<InvoiceModel> al) {

        ArrayList<String> arr = new ArrayList<>();
        for (int x = 0; x < al.size(); x++) {
            arr.add(""
                    + "#" + (x + 1)
                    + "\nTipo: " + al.get(x).getType()
                    + "\nDate: " + al.get(x).getDate()
                    + "\nInicio de facturación: " + al.get(x).getStart_period()
                    + "\nFin de facturación: " + al.get(x).getEnd_period()
                    + "\nConsumo: " + al.get(x).getConsumption()
                    + "\nMonto: " + al.get(x).getAmount());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arr);
        lv_invoice.setAdapter(arrayAdapter);
/*
        AdapterView.OnItemClickListener lvClick = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), "Seleccionado elemento: "+position, Toast.LENGTH_LONG).show();
            }
        };
        lv_invoice.setOnItemClickListener(lvClick);
*/
    }
    private void fillListViewCustomAdapter(){

        for (int x = 0; x < arrIM.size(); x++) {
            String measure="";

           if(arrIM.get(x).getType().equals("electricidad")){
                measure="KW";
            }else if(arrIM.get(x).getType().equals("gas")) {
           measure="Litros";
           }else if(arrIM.get(x).getType().equals("agua")){
               measure="Litros";
           }else if(arrIM.get(x).getType().equals("Telefonia")){
               measure="Mes";
           }else if(arrIM.get(x).getType().equals("Renting Coche")){
               measure="Mes";
           }else if(arrIM.get(x).getType().equals("IBI")){
               measure="Mes";
           }

            ilm.add(new InvoiceListModel());
            ilm.get(x).setType("Tipo: "+arrIM.get(x).getType());
            ilm.get(x).setAmount("Gasto: "+arrIM.get(x).getAmount()+currency);
            ilm.get(x).setDate("Fecha: "+arrIM.get(x).getDate());
            ilm.get(x).setConsumption("Consumo: "+arrIM.get(x).getConsumption()+measure);
            ilm.get(x).setCode(x);
        }

        InvoiceListAdapter ila =new InvoiceListAdapter(this, this, ilm);
        lv_invoice.setAdapter(ila);
    }

/*
    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "TAB 1"+tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();

        if (tabLayout.getSelectedTabPosition()== 0) {
            Toast.makeText(getApplicationContext(), "TAB 1", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "TAB 2", Toast.LENGTH_LONG).show();

        }
    }

 */
@Override
public void onClick(View v) {
    int choice = v.getId();
    switch (v.getId()) {
        case R.id.ibAdd:
            btManual.setVisibility(View.VISIBLE);
            btOCR.setVisibility(View.VISIBLE);

            // Para verse las sombras de los botones, provoca un back negro, cambiar el método back
          //  getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //  Toast.makeText(getApplicationContext(), "idGroup "+idGroup, Toast.LENGTH_LONG).show();

 /*            TODO: que la actividad se torne en escala grises
               new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setPositiveButton("Añadir de forma manual",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
*/

            break;

        case R.id.btManual:
            // abre activity para añadir nuevas facturas manuales
            Intent intent = new Intent(getApplicationContext(), GroupInvoiceAdd.class);
            intent.putExtra("idGroup",idGroup);
            intent.putExtra("groupName",groupName);
            intent.putExtra("userEmail",userEmail);
            startActivity(intent);
            break;

        case R.id.btOCR:
            //TODO: lectura factura por OCR
            break;
    }
}

}