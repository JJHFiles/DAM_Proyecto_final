package com.example.dam_proyecto_final.ui.home.homeui.group_invoice;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.dam_proyecto_final.ui.home.HomeActivity;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui.InvoiceOCRAddActivity;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui.edit_group.GroupInvoiceEditGroup;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

public class GroupInvoiceEmptyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtv_EmptyDescription;
    private TextView txtv_EmptyTitle;

    private ExtendedFloatingActionButton btOCR, btManual;

    private GroupModel groupModel;
    private String userEmail;
    private String userPass;
    private Menu menu;
    ImageButton ibAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_home);
        txtv_EmptyDescription = findViewById(R.id.txtv_EmptyDescription);
        txtv_EmptyTitle = findViewById(R.id.txtv_EmptyTitle);
        ibAdd = findViewById(R.id.ibAdd);
        ibAdd.setOnClickListener(this);


        btManual = findViewById(R.id.btManual);
        btOCR = findViewById(R.id.btOCR);
        btManual.setOnClickListener(this);
        btOCR.setOnClickListener(this);

        Bundle parametros = this.getIntent().getExtras();

        if (parametros != null) {
            groupModel = (GroupModel) parametros.getSerializable("groupModel");
            userEmail = parametros.getString("userEmail", "vacio");
            userPass = parametros.getString("userPass", "vacio");
            Log.d("DEBUGME", "Grupo: " + groupModel.getId());

        } else {
            Toast.makeText(getApplicationContext(), "ERROR GRAVE idGroup = null", Toast.LENGTH_LONG).show();
        }

        this.setTitle(groupModel.getName());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        noInvoices();


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ibAdd) {
            if (btManual.getVisibility() == View.INVISIBLE) {
                btManual.setVisibility(View.VISIBLE);
                btOCR.setVisibility(View.VISIBLE);
            } else {
                btManual.setVisibility(View.INVISIBLE);
                btOCR.setVisibility(View.INVISIBLE);
            }
        } else if (v.getId() == R.id.btManual) {
            // Abre activity para añadir nuevas facturas manuales
            Intent intent = new Intent(getApplicationContext(), GroupInvoiceAdd.class);
//            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("groupModel", groupModel);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("userPass", userPass);
            startActivity(intent);
        } else if (v.getId() == R.id.btOCR) {
            Intent intentScan = new Intent(getApplicationContext(), InvoiceOCRAddActivity.class);
            intentScan.putExtra("groupModel", groupModel);
            intentScan.putExtra("userEmail", userEmail);
            intentScan.putExtra("userPass", userPass);
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
        this.menu=menu;
        applyPermission();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnu_GIHAEditGroup) {
            Intent intent = new Intent(getApplicationContext(), GroupInvoiceEditGroup.class);
            intent.putExtra("idGroup", groupModel.getId());
            intent.putExtra("groupName", groupModel.getName());
            intent.putExtra("currency", groupModel.getCurrency());
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("userPass", userPass);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_rigth, R.anim.right_to_left);

        this.finish();
    }

    public void applyPermission(){
        if(this.groupModel.getRole()>=2) {
            MenuItem item = menu.findItem(R.id.mnu_GIHAEditGroup);
            item.setVisible(false);
            ibAdd.setVisibility(View.INVISIBLE);
        }
    }

}