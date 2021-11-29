package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.GroupInvoiceTab;
import com.example.dam_proyecto_final.web_api.WebApiRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class InvoiceDetail extends AppCompatActivity implements View.OnClickListener {

    String userEmail;
    String userPass;
    InvoiceModel invoice;
    GroupModel groupModel;

    EditText etNum;
    EditText etProvider;
    EditText etType;
    EditText etDateEmition;
    EditText etDateStart;
    EditText etDateEnd;
    EditText etConsumption;
    EditText etCost;
    String file;

    WebApiRequest webApiRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


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
        Button btnDeleteInvoice = findViewById(R.id.btn_invoicedetail_deleteinvoice);
        btnDeleteInvoice.setOnClickListener(this);


        Bundle param = getIntent().getExtras();
        if (param != null) {
            userEmail = param.getString("userEmail", "vacio");
            userPass = param.getString("userPass", "vacio");
            invoice = param.getParcelable("invoice");
            groupModel = (GroupModel) param.getSerializable("groupModel");


            this.setTitle("Factura " + invoice.getIdentifier());
            etNum.setText(invoice.getIdentifier());
            etProvider.setText(invoice.getProvider());
            etType.setText(invoice.getType());
            etDateEmition.setText(invoice.getDate());
            etDateStart.setText(invoice.getStart_period());
            etDateEnd.setText(invoice.getEnd_period());
            etConsumption.setText(String.valueOf(invoice.getConsumption()));
            etCost.setText(String.valueOf(invoice.getAmount()));

            if (groupModel.getRole() < 2){
                btnDeleteInvoice.setVisibility(View.VISIBLE);
            }

        } else {
            onBackPressed();
            Log.d("DEBUGME", "InvoiceDetail: Error grave, falta de parametros");
        }

        webApiRequest = new WebApiRequest(getApplicationContext());
        webApiRequest.getFileInvoice(userEmail, userPass, invoice, new WebApiRequest.WebApiRequestJsonResponseStringListener() {

            @Override
            public void onSuccess(JsonResponseModel response, String data) {
                Log.d("DEBUGME", "InvoiceDetail getFileInvoice " + response.getId() + " " + response.getMessage());
                if (response.getId() == 841) {
                    llShowfile.setVisibility(View.VISIBLE);
                    file = data;
                }
            }

            @Override
            public void onError(JsonResponseModel response) {
                Log.d("DEBUGME", "InvoiceDetail: " + response.getMessage());
               // Toast.makeText(getApplicationContext(), "Se ha producido un error: " + response.getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_invoicedetail_deleteinvoice) {
            webApiRequest.invoiceDelete(userEmail, userPass, invoice, groupModel, new WebApiRequest.WebApiRequestJsonResponseListener() {
                @Override
                public void onSuccess(JsonResponseModel response) {
                    Toast.makeText(getApplicationContext(), getString(R.string.invoice_deleted) + response.getId(), Toast.LENGTH_SHORT).show();
                    Log.d("DEBUGME", getString(R.string.invoice_deleted));
                    Log.d("DEBUGME", "InvoiceDetail: " + response.getMessage());

                    Intent intent = new Intent(getApplicationContext(), GroupInvoiceTab.class);
                    intent.putExtra("groupModel", groupModel);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userPass", userPass);
                    finish();
                    startActivity(intent);
                }

                @Override
                public void onError(JsonResponseModel response) {
                    Log.d("DEBUGME", "InvoiceDetail: " + response.getMessage());
                   // Toast.makeText(getApplicationContext(), "Error: " + response.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (view.getId() == R.id.ll_invoicedetail_showfile) {
            getStringToPdf();
        }
    }

    public void getStringToPdf() {
        File fileout = new File(getExternalFilesDir(null), "temp.pdf");

        try (FileOutputStream fos = new FileOutputStream(fileout)) {

            byte[] decoder = Base64.decode(file, Base64.DEFAULT);

            fos.write(decoder);
            fos.flush();
            fos.close();
            Log.d("DEBUGME", "PDF File Saved");

            Uri uri;
            uri = FileProvider.getUriForFile(this,
                    this.getApplicationContext().getPackageName() +
                            ".provider", fileout);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                    Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
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