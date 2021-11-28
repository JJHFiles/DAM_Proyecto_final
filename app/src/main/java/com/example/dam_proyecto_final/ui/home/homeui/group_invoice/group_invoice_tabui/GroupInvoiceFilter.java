package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui;


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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class GroupInvoiceFilter extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText tiet_GIFStartPeriod;
    private TextInputEditText tiet_GIFEndPeriod;

    private Calendar dateFrom;
    private Calendar dateTo;

    private ArrayList<CheckBox> checkbox;

    private RadioButton rb_GIFConsumption;
    private RadioButton rb_GIFCost;

    private ArrayList<InvoiceModel> invoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_filter);


        //Establecemos titulo al banner y flecha de back
        this.setTitle(getResources().getString(R.string.filter));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Instanciamos vistas
        FlexboxLayout fl_GIFCheckTypeInvoice = findViewById(R.id.fl_GIFCheckTypeInvoice);
        TextInputLayout til_GIFStartPeriod = findViewById(R.id.til_GIFStartPeriod);
        til_GIFStartPeriod.setOnClickListener(this);
        tiet_GIFStartPeriod = findViewById(R.id.tiet_GIFStartPeriod);
        tiet_GIFStartPeriod.setOnClickListener(this);
        TextInputLayout til_GIFEndPeriod = findViewById(R.id.til_GIFEndPeriod);
        til_GIFEndPeriod.setOnClickListener(this);
        tiet_GIFEndPeriod = findViewById(R.id.tiet_GIFEndPeriod);
        tiet_GIFEndPeriod.setOnClickListener(this);

        rb_GIFConsumption = findViewById(R.id.rb_GIFConsumption);
        rb_GIFCost = findViewById(R.id.rb_GIFCost);

        Button btn_GIFClear = findViewById(R.id.btn_GIFClear);
        btn_GIFClear.setOnClickListener(this);
        Button btn_GIFFilter = findViewById(R.id.btn_GIFFilter);
        btn_GIFFilter.setOnClickListener(this);

        //Inicializar calendarios
        dateFrom = Calendar.getInstance();
        dateFrom.setTimeInMillis(Long.MIN_VALUE);
        dateTo = Calendar.getInstance();


        //Obtenemos la lista de facturas
        invoices = getIntent().getExtras().getParcelableArrayList("invoices");

        //Instanciamos los Checkbox programáticamente en base a los tipos de facturas obtenidos
        ArrayList<String> types = new ArrayList<>();
        checkbox = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());

                //Añadimos los checkbox
                CheckBox cb = new CheckBox(new ContextThemeWrapper(getBaseContext(), R.style.CheckRadio), null, 0);
                int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        pixels
                );
                pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
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

        // Obtenemos si había un tipo a representar marcado previamente
        int typeChart = getIntent().getExtras().getInt("typeChart", -1);
        if (typeChart == -1) {
            rb_GIFConsumption.setChecked(true);
        } else {
            RadioButton rbSelected = findViewById(typeChart);
            rbSelected.setChecked(true);
        }

        // Obtenemos los typeInvoices marcados previamente
        ArrayList<String> typeInvoices = getIntent().getExtras().getStringArrayList("cbSelected");
        if (typeInvoices != null) {
            for (CheckBox cb : checkbox) {
                if (typeInvoices.contains(cb.getText().toString())) {
                    cb.setChecked(true);
                }
            }
        } else {
            for (CheckBox cb : checkbox) {
                cb.setChecked(true);
            }
        }
        // Obtenemos las fechas marcadas previamente
        Calendar dateFromPrevious = (Calendar) getIntent().getExtras().getSerializable("dateFrom");
        Calendar dateToPrevious = (Calendar) getIntent().getExtras().getSerializable("dateTo");
        if (dateFromPrevious != null && dateToPrevious != null) {
            dateFrom = dateFromPrevious;
            dateTo = dateToPrevious;

            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
            tiet_GIFStartPeriod.setText(format.format(dateFrom.getTime()));
            tiet_GIFEndPeriod.setText(format.format(dateTo.getTime()));
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                for (CheckBox cb : checkbox) {
                    cb.setChecked(false);
                }
                rb_GIFConsumption.setChecked(false);
                rb_GIFCost.setChecked(false);
                tiet_GIFStartPeriod.setText("");
                tiet_GIFEndPeriod.setText("");
                break;
            case R.id.btn_GIFFilter:

                //Comprueba si algún checkbox de tipo está seleccionado
                ArrayList<String> cbSelected = new ArrayList<>();
                boolean cbChecked = false;
                for (CheckBox cb : checkbox) {
                    if (cb.isChecked()) {
                        cbChecked = true;
                        cbSelected.add(cb.getText().toString());
                    }
                }
                // Comprobamos que radio button está seleccionado
                int rbSelected = -1;
                if (rb_GIFConsumption.isChecked()) {
                    rbSelected = rb_GIFConsumption.getId();
                } else if (rb_GIFCost.isChecked()) {
                    rbSelected = rb_GIFCost.getId();
                }
                if (cbChecked) {
                    //Comprobamos que la fecha desde no sea superior a hasta
                    if (dateFrom.before(dateTo)) {
                        // Cogemos las facturas filtradas según parametros
                        ArrayList<InvoiceModel> arrIMFilter = filterInvoice(cbSelected);
                        if (!(arrIMFilter.size() == 0)) {
                            // Si el filtro no es 0 pasamos los parametros filtrados a la activity
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("arrIMFilter", arrIMFilter);
                            returnIntent.putExtra("typeChart", rbSelected);
                            returnIntent.putExtra("cbSelected", cbSelected);
                            // Comprobamos que las fechas no sean por defecto para pasarlas
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(Long.MIN_VALUE);
                            if (!dateFrom.equals(cal)) {
                                returnIntent.putExtra("dateFrom", dateFrom);
                                returnIntent.putExtra("dateTo", dateTo);
                            }
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Toast.makeText(this, getString(R.string.warning_filter_without_invoice), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.warning_period_not_allowed), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.warning_filter_not_selected), Toast.LENGTH_LONG).show();
                }

                break;
        }
        //Configuramos los datepicker

    }

    private ArrayList<InvoiceModel> filterInvoice(ArrayList<String> cbSelected) {
        ArrayList<InvoiceModel> invoicesFilter = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            //Creamos el campo Calendar para comparar fecha emision con rango establecido
            String[] dateEmitionStr = i.getDate().split("-");
            Calendar dateEmition = Calendar.getInstance(Locale.getDefault());
            dateEmition.set(Integer.parseInt(dateEmitionStr[0]),
                    Integer.parseInt(dateEmitionStr[1]) - 1,
                    Integer.parseInt(dateEmitionStr[2]));
            dateEmition.getTime();
            //Si la factura está en rango la incorporamos a nueva lista de retorno
            if (dateFrom.before(dateEmition) && dateTo.after(dateEmition)) {
                // Si la factura es de algún tipo seleccionado
                if (cbSelected.contains(i.getType())) {
                    invoicesFilter.add(i);
                }
            }
        }
        return invoicesFilter;
    }

    private void addCalendar(Calendar calendar, TextInputEditText tiet) {
        //TODO cambiar idioma de los textos
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}


