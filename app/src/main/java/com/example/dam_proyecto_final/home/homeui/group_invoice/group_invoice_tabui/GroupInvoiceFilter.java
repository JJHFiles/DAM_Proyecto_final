package com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class GroupInvoiceFilter extends AppCompatActivity implements View.OnClickListener {

    private FlexboxLayout fl_GIFCheckTypeInvoice;
    private TextInputEditText tiet_GIFStartPeriod;
    private TextInputEditText tiet_GIFEndPeriod;
    private TextInputLayout til_GIFStartPeriod;
    private TextInputLayout til_GIFEndPeriod;

    private Calendar dateFrom;
    private Calendar dateTo;

    private ArrayList<CheckBox> checkbox;

    private RadioButton rb_GIFConsumption;
    private RadioButton rb_GIFCost;

    private Button btn_GIFClear;
    private Button btn_GIFFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_filter);


        //Establecemos titulo al banner y flecha de back
        this.setTitle(getResources().getString(R.string.filter));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Instanciamos vistas
        fl_GIFCheckTypeInvoice = findViewById(R.id.fl_GIFCheckTypeInvoice);
        til_GIFStartPeriod = findViewById(R.id.til_GIFStartPeriod);
        til_GIFStartPeriod.setOnClickListener(this);
        tiet_GIFStartPeriod = findViewById(R.id.tiet_GIFStartPeriod);
        tiet_GIFStartPeriod.setOnClickListener(this);
        til_GIFEndPeriod = findViewById(R.id.til_GIFEndPeriod);
        til_GIFEndPeriod.setOnClickListener(this);
        tiet_GIFEndPeriod = findViewById(R.id.tiet_GIFEndPeriod);
        tiet_GIFEndPeriod.setOnClickListener(this);

        rb_GIFConsumption = findViewById(R.id.rb_GIFConsumption);
        rb_GIFCost = findViewById(R.id.rb_GIFCost);

        btn_GIFClear = findViewById(R.id.btn_GIFClear);
        btn_GIFClear.setOnClickListener(this);
        btn_GIFFilter = findViewById(R.id.btn_GIFFilter);
        btn_GIFFilter.setOnClickListener(this);

        //Inicializar calendarios
        dateFrom  = Calendar.getInstance();
        dateTo  = Calendar.getInstance();

        //Obtenemos la lista de facturas
        ArrayList<InvoiceModel> invoices = getIntent().getExtras().getParcelableArrayList("invoices");

        //Instanciamos los Checkbox programáticamente en base a los tipos de facturas obtenidos
        ArrayList<String> types = new ArrayList<String>();
        checkbox = new ArrayList<CheckBox>();
        for (InvoiceModel i : invoices){
            if (!types.contains(i.getType())){
                types.add(i.getType());

                //Añadimos los checkbox
                CheckBox cb = new CheckBox(new ContextThemeWrapper(getBaseContext(), R.style.CheckRadio), null, 0);
                int pixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        pixels
                );
                pixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                params.bottomMargin = pixels;
                params.rightMargin = pixels;
                cb.setLayoutParams(params);
                cb.setTextSize(14);
                cb.setText(i.getType());
                fl_GIFCheckTypeInvoice.addView(cb);

                //Añadimos a la lista
                checkbox.add(cb);

            }

        }


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //DatePicker
            case R.id.tiet_GIFStartPeriod:
                Log.d("DEBUGME", "GroupInvoiceFilter onclick! desde");
                addCalendar(dateFrom, tiet_GIFStartPeriod);

                break;
            case R.id.tiet_GIFEndPeriod:
                addCalendar(dateTo, tiet_GIFEndPeriod);
                break;
            //BottomAppBar
            case R.id.btn_GIFClear:
                    for (CheckBox cb : checkbox){
                        cb.setChecked(false);
                    }
                    rb_GIFConsumption.setChecked(false);
                    rb_GIFCost.setChecked(false);
                    tiet_GIFStartPeriod.setText("");
                    tiet_GIFEndPeriod.setText("");
                break;
            case R.id.btn_GIFFilter:

                //Comprueba si algún checkbox de tipo está seleccionado
                boolean cbChecked = false;
                for (CheckBox cb : checkbox){
                    if (cb.isChecked()){
                        cbChecked = true;
                    }
                }
                if(cbChecked){
                    // Comprueba si hay un tipo de gráfica seleccionada
                    if ((rb_GIFConsumption.isChecked() || rb_GIFCost.isChecked())){
                        // Comprueba si las fechas no están vacias
                        if (!tiet_GIFStartPeriod.getText().toString().equals("") && tiet_GIFStartPeriod.getText().toString() != null &&
                                !tiet_GIFEndPeriod.getText().toString().equals("") && tiet_GIFEndPeriod.getText().toString() != null) {
                            //Comprobamos que la fecha desde no sea superior a hasta
                            if (dateFrom.before(dateTo)){
                                // TODO recrear la lista con las fechas comprendidas
                                // TODO devolver al padre los resultados
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result", "aaa");
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            } else {
                                Toast.makeText(this, "La fecha \"Desde\" no puede ser superior a la fecha \"Hasta\"", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No ha seleccionado el rango de fechas", Toast.LENGTH_LONG).show();
                        }
                    } else{
                        Toast.makeText(this, "No ha seleccionado ningún tipo de dato a representar", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "No ha seleccionado ningún tipo de factura", Toast.LENGTH_LONG).show();
                }




                break;
        }
        //Configuramos los datepicker

    }

    private void addCalendar(Calendar calendar, TextInputEditText tiet){
        //TODO cambiar idioma de los textos
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date)
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String formatted = format.format(calendar.getTime());
                tiet.setText(formatted);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), String.valueOf(R.string.select_date));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

}


