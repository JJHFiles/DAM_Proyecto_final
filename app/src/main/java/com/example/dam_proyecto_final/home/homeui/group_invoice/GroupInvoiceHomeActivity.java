package com.example.dam_proyecto_final.home.homeui.group_invoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_proyecto_final.Model.InvoiceModel;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.web_api.WebApiRequest;

import java.util.ArrayList;

public class GroupInvoiceHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtv_EmptyDescription, txtv_EmptyTitle;
    private ImageButton ibAdd;
    private Button btManual, btOCR;

    private ListView lv_invoice;

    private WebApiRequest webApiRequest;
    private String userEmail, idGroup, groupName;
    private ArrayList<InvoiceModel> invoiceModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_home);
        txtv_EmptyDescription = findViewById(R.id.txtv_EmptyDescription);
        txtv_EmptyTitle = findViewById(R.id.txtv_EmptyTitle);
        ibAdd = findViewById(R.id.ibAdd);
        ibAdd.setOnClickListener(this);

        lv_invoice = findViewById(R.id.lv_invoice);

        btManual = findViewById(R.id.btManual);
        btOCR = findViewById(R.id.btOCR);
        btManual.setOnClickListener(this);
        btOCR.setOnClickListener(this);

/* No hacen falta, se recibe por Bundle.
        SharedPreferences preferencias = getApplicationContext().getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", "vacio1");
*/

        Bundle parametros = this.getIntent().getExtras();

        if (parametros != null) {
            idGroup = parametros.getString("idGroup", "vacio");
            groupName = parametros.getString("groupName", "vacio");
            userEmail = parametros.getString("userEmail", "vacio");
            Toast.makeText(getApplicationContext(), "idGroup " + idGroup, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "ERROR GRAVE idGroup = null", Toast.LENGTH_LONG).show();
        }
       this.setTitle(groupName);

        noInvoices();

    }

    @Override
    public void onClick(View v) {
        int choice = v.getId();
        switch (v.getId()) {
            case R.id.ibAdd:
                btManual.setVisibility(View.VISIBLE);
                btOCR.setVisibility(View.VISIBLE);
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


    public void noInvoices() {
        txtv_EmptyDescription.setText("Aún no tienes ninguna factura añadida a este grupo.\n" +
                "Cuando añadas una esta te aparecerá aquí.");
        txtv_EmptyDescription.setVisibility(View.VISIBLE);
        txtv_EmptyTitle.setVisibility(View.VISIBLE);
        this.setTitle("Nombre del grupo seleccionado");
    }
}