package com.example.dam_proyecto_final.ui.home.homeui.group_invoice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.utility.CalendarUtility;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.textfield.TextInputEditText;

public class GroupInvoiceAdd extends AppCompatActivity implements View.OnClickListener {
    private CalendarUtility cuStartPeriod, cuEndPeriod, cuDate;
    private InvoiceModel invoiceModel;
    private TextInputEditText
            tietInvoiceNum,
            tietProvider,
            tietDate,
            tietStartPeriod,
            tietEndPeriod,
            tietConsumption,
            tietAmount;
    private String typeSelection;
    private AutoCompleteTextView actvInvoiceType;
    private Button btNew;

//    private int idGroup;
//    private String groupName,,currency;
    private GroupModel groupModel;
    private String userEmail;
    private String userPass;
    private WebApiRequest webApiRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_add);
        setTitle("Insertar nueva factura");

        webApiRequest = new WebApiRequest(getApplicationContext());

        tietInvoiceNum = findViewById(R.id.tiet_GIFStarPeriod);
        tietProvider = findViewById(R.id.tiet_provider);
        tietDate = findViewById(R.id.tiet_date);
        tietStartPeriod = findViewById(R.id.tiet_startPeriod);
        tietEndPeriod = findViewById(R.id.tiet_endPeriod);
        tietConsumption = findViewById(R.id.tiet_Consumption);
        tietAmount = findViewById(R.id.tiet_Amount);
        actvInvoiceType =findViewById(R.id.actv_invoiceType);
        btNew =findViewById(R.id.bt_New);
        btNew.setOnClickListener(this);

        Bundle param= this.getIntent().getExtras();
        if (param != null) {
            groupModel = (GroupModel) param.getSerializable("groupModel");
            userEmail = param.getString("userEmail", "vacio");
            userPass = param.getString("userPass", "vacio");

        }
        loadInvoiceType();

        // Se preparan los escuchadores onFocusChangeListener sobre los TextInputEditText
        cuStartPeriod = new CalendarUtility(this, R.id.tiet_startPeriod);
        listenerDateStartPeriod();

        cuEndPeriod = new CalendarUtility(this, R.id.tiet_endPeriod);
        listenerDateEndPeriod();

        cuDate = new CalendarUtility(this, R.id.tiet_date);
        listenerDate();

    }

    public void loadInvoiceType() {

        //Asignamos lista a DropDowns
        String[] type = {"Electricidad", "Agua", "Gas", "Telefonía"};
        ArrayAdapter typeAdapter = new ArrayAdapter(this, R.layout.activity_group_add_dropdown_item, type);
        actvInvoiceType.setAdapter(typeAdapter);
        actvInvoiceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                typeSelection = parent.getItemAtPosition(position).toString();
            }
        });
    }

    public void listenerDateStartPeriod() {
        cuStartPeriod.getDate();
    }

    public void listenerDateEndPeriod() {
        cuEndPeriod.getDate();
    }

    public void listenerDate() {
        cuDate.getDate();
    }

    @Override
    public void onClick(View v) {
        if (checkFields()){
            if(v.getId()==R.id.bt_New){
                // TODO añadir provider al añadir factura
                invoiceModel=new InvoiceModel(
                        tietInvoiceNum.getText().toString(),
                        tietProvider.getText().toString(),
                        typeSelection,
                        tietDate.getText().toString(),
                        tietStartPeriod.getText().toString(),
                        tietEndPeriod.getText().toString(),
                        Double.parseDouble(tietConsumption.getText().toString()),
                        Double.parseDouble(tietAmount.getText().toString()),
                        "0",
                        groupModel.getId()
                );
                insertInvoice(invoiceModel);
            }
        }

    }

    private boolean checkFields() {
        if(tietInvoiceNum.getText().toString() == null || tietInvoiceNum.getText().toString().equals("")){
            Toast.makeText(this, "Número de factura no indicado, por favor rellene todos los campos"
                    , Toast.LENGTH_LONG);
            return false;
        }
        if(tietProvider.getText().toString() == null || tietProvider.getText().toString().equals("")){
            Toast.makeText(this, "Proveedor no indicado, por favor rellene todos los campos"
                    , Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }

    public void insertInvoice(InvoiceModel im){

        webApiRequest.insertInvoice(im, new WebApiRequest.WebApiRequestJsonObjectListener() {
            @Override
            public void onSuccess(int id, String message) {
                if (id > 0) {
                    Log.d("DEBUGME", "insertInvoice onSucess: " + id + " " + message);

                    Toast.makeText(getApplicationContext(), "Factura insertada con éxito: " + id, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), GroupInvoiceTab.class);
                    intent.putExtra("groupModel", groupModel);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userPass", userPass);
                    startActivity(intent);
                } else if (id < 0) {
                    Log.d("DEBUGME", "insertInvoice onSucess: " + id + " " + message);
                    Toast.makeText(getApplicationContext(), "Error al insertar factura. Codigo de error: " + id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(int id, String message) {
                Log.d("DEBUGME", "insertInvoice onerror: " + id + " " + message);
                Toast.makeText(getApplicationContext(), "Error al insertar factura. Codigo de error: " + id, Toast.LENGTH_LONG).show();
            }
        });
    }

}