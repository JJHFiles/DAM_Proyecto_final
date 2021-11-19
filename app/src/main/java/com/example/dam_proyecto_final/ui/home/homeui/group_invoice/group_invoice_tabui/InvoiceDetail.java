package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.InvoiceModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InvoiceDetail extends AppCompatActivity implements View.OnClickListener {

    String userEmail;
    String userPass;

    EditText etNum;
    EditText etProvider;
    EditText etType;
    EditText etDateEmition;
    EditText etDateStart;
    EditText etDateEnd;
    EditText etConsumption;
    EditText etCost;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        etNum = findViewById(R.id.et_invoicedetail_num);
        etProvider = findViewById(R.id.et_invoicedetail_provider);
        etType = findViewById(R.id.et_invoicedetail_type);
        etDateEmition = findViewById(R.id.et_invoicedetail_dateemition);
        etDateStart = findViewById(R.id.et_invoicedetail_datestart);
        etDateEnd = findViewById(R.id.et_invoicedetail_dateend);
        etConsumption = findViewById(R.id.et_invoicedetail_consumption);
        etCost = findViewById(R.id.et_invoicedetail_cost);
        LinearLayout llShowfile = findViewById(R.id.ll_invoicedetail_showfile);
        llShowfile.setOnClickListener(this);


        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            userEmail = parametros.getString("userEmail", "vacio");
            userPass = parametros.getString("userPass", "vacio");
            InvoiceModel invoice = (InvoiceModel) parametros.getParcelable("invoice");


            this.setTitle("Factura " + invoice.getIdentifier());
            etNum.setText(invoice.getIdentifier());
            etProvider.setText(invoice.getProvider());
            etType.setText(invoice.getType());
            etDateEmition.setText(invoice.getDate());
            etDateStart.setText(invoice.getStart_period());
            etDateEnd.setText(invoice.getEnd_period());
            etConsumption.setText(String.valueOf(invoice.getConsumption()));
            etCost.setText(String.valueOf(invoice.getAmount()));

        } else {
            Log.d("DEBUGME", "GroupInvoiceTab: ERROR GRAVE idGroup = null");
        }
    }


    @Override
    public void onClick(View view) {

    }

    public String getStringPdf (Uri filepath){
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream =  getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}