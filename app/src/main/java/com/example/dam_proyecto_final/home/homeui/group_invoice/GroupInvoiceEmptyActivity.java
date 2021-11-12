package com.example.dam_proyecto_final.home.homeui.group_invoice;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.InvoiceOCRAddActivity;
import com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui.edit_group.GroupInvoiceEditGroup;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

public class GroupInvoiceEmptyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtv_EmptyDescription;
    private TextView txtv_EmptyTitle;

    private ExtendedFloatingActionButton btOCR,btManual;

    private String userEmail;
    private int idGroup;
    private String groupName;
    private String userPass;
    private String currency;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_home);
        txtv_EmptyDescription = findViewById(R.id.txtv_EmptyDescription);
        txtv_EmptyTitle = findViewById(R.id.txtv_EmptyTitle);
        ImageButton ibAdd = findViewById(R.id.ibAdd);
        ibAdd.setOnClickListener(this);


        btManual = findViewById(R.id.btManual);
        btOCR = findViewById(R.id.btOCR);
        btManual.setOnClickListener(this);
        btOCR.setOnClickListener(this);

        Bundle parametros = this.getIntent().getExtras();

        if (parametros != null) {
            GroupModel group = (GroupModel) parametros.getSerializable("group");
            idGroup = group.getId();
            groupName = group.getName();
            currency = group.getCurrency();
            userEmail = parametros.getString("userEmail", "vacio");
            userPass = parametros.getString("userPass", "vacio");
            Toast.makeText(getApplicationContext(), "idGroup " + idGroup, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "ERROR GRAVE idGroup = null", Toast.LENGTH_LONG).show();
        }

        this.setTitle(groupName);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        noInvoices();


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ibAdd){
            btManual.setVisibility(View.VISIBLE);
            btOCR.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.btManual){
            // Abre activity para añadir nuevas facturas manuales
            Intent intent = new Intent(getApplicationContext(), GroupInvoiceAdd.class);
            intent.putExtra("idGroup", idGroup);
            intent.putExtra("groupName", groupName);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        }
        else if (v.getId() == R.id.btOCR){
            //TODO: lectura factura por OCR
            Intent intentScan = new Intent(getApplicationContext(), InvoiceOCRAddActivity.class);
            intentScan.putExtra("idGroup", idGroup);
            startActivity(intentScan);
        }

    }


    public void noInvoices() {
        txtv_EmptyDescription.setText(R.string.empty_invoices_text);
        txtv_EmptyDescription.setVisibility(View.VISIBLE);
        txtv_EmptyTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_invoice_empty_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        if (item.getItemId() == R.id.mnu_GIHAEditGroup) {
            Intent intent = new Intent(getApplicationContext(), GroupInvoiceEditGroup.class);
            intent.putExtra("idGroup", idGroup);
            intent.putExtra("groupName", groupName);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("userPass", userPass);
            intent.putExtra("currency", currency);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}