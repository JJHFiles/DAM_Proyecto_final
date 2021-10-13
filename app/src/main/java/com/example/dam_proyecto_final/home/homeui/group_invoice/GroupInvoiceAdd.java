package com.example.dam_proyecto_final.home.homeui.group_invoice;

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
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.utility.CalendarUtility;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.textfield.TextInputEditText;

public class GroupInvoiceAdd extends AppCompatActivity implements View.OnClickListener {
    private CalendarUtility cuStartPeriod, cuEndPeriod, cuDate;
    private InvoiceModel invoiceModel;
    private TextInputEditText
            tiet_invoiceNum,
            tiet_provider,
            tiet_date,
            tiet_startPeriod,
            tiet_endPeriod,
            tiet_Consumption,
            tiet_Amount;
    private String typeSelection;
    private AutoCompleteTextView actv_invoiceType;
    private Button bt_New;
    private String idGroup,groupName,userEmail,currency;

    private WebApiRequest webApiRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_add);
        setTitle("Insertar nueva factura");

        webApiRequest = new WebApiRequest(getApplicationContext());

        tiet_invoiceNum = findViewById(R.id.tiet_GIFStarPeriod);
        tiet_provider = findViewById(R.id.tiet_provider);
        tiet_date = findViewById(R.id.tiet_date);
        tiet_startPeriod = findViewById(R.id.tiet_startPeriod);
        tiet_endPeriod = findViewById(R.id.tiet_endPeriod);
        tiet_Consumption = findViewById(R.id.tiet_Consumption);
        tiet_Amount = findViewById(R.id.tiet_Amount);
        actv_invoiceType=findViewById(R.id.actv_invoiceType);
        bt_New=findViewById(R.id.bt_New);
        bt_New.setOnClickListener(this);

        Bundle param= this.getIntent().getExtras();
        if (param != null) {
            idGroup = param.getString("idGroup", "vacio");
            groupName = param.getString("groupName", "vacio");
            userEmail = param.getString("userEmail", "vacio");
            currency=param.getString("currency", "vacio");
        }
        loadInvoiceType();

// Se preparan los escuchadores onFocusChangeListener sobre los TextInputEditText
        cuStartPeriod = new CalendarUtility(this, R.id.tiet_startPeriod);
        listenerDateStartPeriod();

        cuEndPeriod = new CalendarUtility(this, R.id.tiet_endPeriod);
        listenerDateEndPeriod();

        cuDate = new CalendarUtility(this, R.id.tiet_date);
        listenerDate();





     /*   final EditText password1 = (EditText) layout.findViewById(R.id.EditText_Pwd1);
        final EditText password2 = (EditText) layout.findViewById(R.id.EditText_Pwd2);
        final TextView error = (TextView) layout.findViewById(R.id.TextView_PwdProblem);
*/


    }

    public void loadInvoiceType() {

        //Asignamos lista a DropDowns
        String[] type = {"Luz", "Agua", "Gas", "Telefonía"};
        ArrayAdapter typeAdapter = new ArrayAdapter(this, R.layout.activity_group_add_dropdown_item, type);
        actv_invoiceType.setAdapter(typeAdapter);
        actv_invoiceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        if(v.getId()==R.id.bt_New){
            invoiceModel=new InvoiceModel(
                    tiet_invoiceNum.getText().toString(),
                    typeSelection,
                    tiet_date.getText().toString(),
                    tiet_startPeriod.getText().toString(),
                    tiet_endPeriod.getText().toString(),
                    Double.parseDouble(tiet_Consumption.getText().toString()),
                    Double.parseDouble(tiet_Amount.getText().toString()),
                    "0",
                    Integer.parseInt(idGroup)
            );
            insertInvoice(invoiceModel);
        }
    }
    public void insertInvoice(InvoiceModel im){

        webApiRequest.insertInvoice(im, new WebApiRequest.WebApiRequestJsonObjectListener() {
            @Override
            public void onSuccess(int id, String message) {
                if (id > 0) {
                    Log.d("DEBUGME", "insertInvoice onSucess: " + id + " " + message);

                    Toast.makeText(getApplicationContext(), "Factura insertada con éxito: " + id, Toast.LENGTH_LONG).show();
                    // bloqueado boton de nueva factura para no insertar la misma varias veces
                    //bt_New.setEnabled(false);

                } else if (id < 0) {
                    Log.d("DEBUGME", "insertInvoice onSucess: " + id + " " + message);
                    Toast.makeText(getApplicationContext(), "Error al insertar. Codigo de error: " + id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(int id, String message) {
                Log.d("DEBUGME", "insertInvoice onerror: " + id + " " + message);
                Toast.makeText(getApplicationContext(), "Error al insertar. Codigo de error: " + id, Toast.LENGTH_LONG).show();
            }
        });
    }

}