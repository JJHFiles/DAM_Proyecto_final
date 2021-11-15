package com.example.dam_proyecto_final.ui.home.homeui.group_invoice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GroupInvoiceAdd extends AppCompatActivity implements View.OnClickListener, TextWatcher {

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

    private GroupModel groupModel;
    private String userEmail;
    private String userPass;
    private WebApiRequest webApiRequest;

    private Calendar dateEmition = Calendar.getInstance();
    private Calendar dateStart = Calendar.getInstance();
    private Calendar dateEnd = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_add);
        setTitle("Insertar nueva factura");

        webApiRequest = new WebApiRequest(getApplicationContext());

        tietInvoiceNum = findViewById(R.id.tiet_GIFStarPeriod);
        tietInvoiceNum.addTextChangedListener(this);
        tietProvider = findViewById(R.id.tiet_provider);
        tietProvider.addTextChangedListener(this);
        tietDate = findViewById(R.id.tiet_date);
        tietDate.addTextChangedListener(this);
        tietDate.setOnClickListener(this);
        tietStartPeriod = findViewById(R.id.tiet_startPeriod);
        tietStartPeriod.addTextChangedListener(this);
        tietStartPeriod.setOnClickListener(this);
        tietEndPeriod = findViewById(R.id.tiet_endPeriod);
        tietEndPeriod.addTextChangedListener(this);
        tietEndPeriod.setOnClickListener(this);
        tietConsumption = findViewById(R.id.tiet_Consumption);
        tietConsumption.addTextChangedListener(this);
        tietAmount = findViewById(R.id.tiet_Amount);
        tietAmount.addTextChangedListener(this);
        actvInvoiceType = findViewById(R.id.actv_invoiceType);
        actvInvoiceType.addTextChangedListener(this);
        btNew = findViewById(R.id.bt_New);
        btNew.setOnClickListener(this);

        Bundle param= this.getIntent().getExtras();
        if (param != null) {
            groupModel = (GroupModel) param.getSerializable("groupModel");
            userEmail = param.getString("userEmail", "vacio");
            userPass = param.getString("userPass", "vacio");

        }
        loadInvoiceType();

    }

    public void loadInvoiceType() {
        //Asignamos lista a DropDowns
        String[] type = {"Electricidad", "Agua", "Gas", "Telefonía"};
        ArrayAdapter typeAdapter = new ArrayAdapter(this, R.layout.activity_group_add_dropdown_item, type);
        actvInvoiceType.setAdapter(typeAdapter);
        actvInvoiceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                typeSelection = parent.getItemAtPosition(position).toString();
                btNew.setEnabled(checkFields());
            }
        });
    }

    private void addCalendar(Calendar calendar, TextInputEditText tiet) {
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date)
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                calendar.setTimeInMillis((Long) selection);

                DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                String formatted = format.format(calendar.getTime());
                tiet.setText(formatted);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), String.valueOf(R.string.select_date));
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_New) {
            if (dateStart.before(dateEnd)){
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                InvoiceModel invoiceModel = new InvoiceModel(
                        tietInvoiceNum.getText().toString(),
                        tietProvider.getText().toString(),
                        typeSelection,
                        formater.format(dateEmition.getTime()),
                        formater.format(dateStart.getTime()),
                        formater.format(dateEnd.getTime()),
                        Double.parseDouble(tietConsumption.getText().toString()),
                        Double.parseDouble(tietAmount.getText().toString()),
                        "0",
                        groupModel.getId()
                );
                insertInvoice(invoiceModel);
                btNew.setClickable(false);
            } else {
                tietStartPeriod.setText("");
                tietEndPeriod.setText("");
                Toast.makeText(this, "El periodo de fin debe ser superior al periodo de inicio",
                        Toast.LENGTH_LONG).show();
                btNew.setEnabled(false);
            }
        } else if (v.getId() == R.id.tiet_date) {
            addCalendar(dateEmition, (TextInputEditText) v);
        } else if (v.getId() == R.id.tiet_startPeriod){
            addCalendar(dateStart, (TextInputEditText) v);
        } else if ( v.getId() == R.id.tiet_endPeriod){
            addCalendar(dateEnd, (TextInputEditText) v);
        }
    }

    private boolean checkFields() {
        if(tietInvoiceNum.getText().toString().equals("")){
            return false;
        }
        if(tietProvider.getText().toString().equals("")){
            return false;
        }
        if(typeSelection == null || typeSelection.equals("")){
            return false;
        }
        if(tietDate.getText().toString().equals("")){
            return false;
        }
        if(tietStartPeriod.getText().toString().equals("")){
            return false;
        }
        if(tietEndPeriod.getText().toString().equals("")){
            return false;
        }
        // TODO si los campos periodo estan metidos comprobar que el inicio no sea superior al fin
        if(tietConsumption.getText().toString().equals("")){
            return false;
        }
        if(tietAmount.getText().toString().equals("")){
            return false;
        }

        return true;
    }

    public void insertInvoice(InvoiceModel im){
        webApiRequest.insertInvoice(userEmail, userPass, im, new WebApiRequest.WebApiRequestJsonObjectListener() {
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
                btNew.setClickable(false);
            }
        });
    }


    // Métodos para habilitar el botón con el form completo
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (checkFields()) {
            btNew.setEnabled(true);
        } else {
            btNew.setEnabled(false);
        }
    }
}