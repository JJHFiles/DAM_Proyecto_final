package com.example.dam_proyecto_final.home.homeui.groupui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dam_proyecto_final.LoginActivity;
import com.example.dam_proyecto_final.Model.JsonResponseModel;
import com.example.dam_proyecto_final.Model.MemberModel;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.home.HomeActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class GroupAddActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lstv_Members;
    private ArrayList<MemberModel> members;
    private TextInputEditText edt_AddMember;
    private TextInputEditText edt_AGAGroupName;
    private TextInputEditText edt_AGADescription;
    private ImageButton imgb_AddMember;
    private MembersAdapter membersAdapter;
    private AutoCompleteTextView dd_AGACurrency;
    private AutoCompleteTextView dd_AGARole;
//    private Button btn_AGACancel;
    private Button btn_AGAAdd;
    private ArrayAdapter rolesAdapter;
    private String roleSelection;
    private String currencySelection;
    private String userEmail;
    private String userPass;
    private WebApiRequest webApiRequest;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);

        context = this;
        webApiRequest = new WebApiRequest(context);

        //Editamos la barra superior con nombre y botón back
        getSupportActionBar().setTitle("Crear grupo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Cogemos el usuario/contraseña para las consultas
        SharedPreferences preferencias = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", "vacio");
        userPass = preferencias.getString("pass", "vacio");

        //Asignamos lista a DropDowns
        String[] currencys = {"EUR", "USD", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "BRL"};
        ArrayAdapter currencyAdapter = new ArrayAdapter(this, R.layout.activity_group_add_dropdown_item, currencys);
        dd_AGACurrency = findViewById(R.id.dd_AGACurrency);
        dd_AGACurrency.setAdapter(currencyAdapter);
        dd_AGACurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                currencySelection = (String) parent.getItemAtPosition(position);
            }
        });

        String[] roles = {getString(R.string.role_admin), getString(R.string.role_editor), getString(R.string.role_reader)};
        rolesAdapter = new ArrayAdapter(this, R.layout.activity_group_add_dropdown_item, roles);
        dd_AGARole = findViewById(R.id.dd_AGARole);
        dd_AGARole.setAdapter(rolesAdapter);
        dd_AGARole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                roleSelection = (String) parent.getItemAtPosition(position);
            }
        });

        //EditText de inclusión de miembro
        edt_AddMember = findViewById(R.id.edt_AddMember);
        imgb_AddMember = findViewById(R.id.imgb_AGAAddMember);
        imgb_AddMember.setOnClickListener(this);
        edt_AGAGroupName = findViewById(R.id.edt_AGAGroupName);
        edt_AGADescription = findViewById(R.id.edt_AGADescription);

        //ListView de miembros
        lstv_Members = findViewById(R.id.lstv_AGAMembers);
        members = new ArrayList<MemberModel>();
        membersAdapter = new MembersAdapter(this, members, lstv_Members);
        lstv_Members.setAdapter(membersAdapter);

        //Buttons
//        btn_AGACancel = findViewById(R.id.btn_AGACancel);
//        btn_AGACancel.setOnClickListener(this);
        btn_AGAAdd = findViewById(R.id.btn_AGAAdd);
        btn_AGAAdd.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
    //TODO revisar que el elemento no esté ya en la lista
        if (view.getId() == R.id.imgb_AGAAddMember){
            //Comprobamos que el email y el rol no esten vacios
            if (Patterns.EMAIL_ADDRESS.matcher(edt_AddMember.getText().toString()).matches()) {
                if (!edt_AddMember.getText().toString().equals("") && roleSelection != null) {
                    //Comprobamos que no haya ya 10 usuario en la lista (máximo 10 usuarios por grupo)
                    if (members.size() < 10) {
                        //Comprobar si el mail existe
                        webApiRequest.getIfEmailExist(edt_AddMember.getText().toString(), new WebApiRequest.WebApiRequestJsonResponseListener() {
                            @Override
                            public void onSuccess(JsonResponseModel response) {
                                int role = 2;
                                if (roleSelection.toString().equals(getString(R.string.role_admin))){
                                    role = 0;
                                } else if (roleSelection.toString().equals(getString(R.string.role_editor))){
                                    role = 1;
                                } else if (roleSelection.toString().equals(getString(R.string.role_reader))){
                                    role = 2;
                                }
                                members.add(new MemberModel(edt_AddMember.getText().toString(), role));
                                membersAdapter.notifyDataSetChanged();
                                edt_AddMember.setText("");
                                setListViewHeightBasedOnChildren(lstv_Members);
                            }

                            @Override
                            public void onError(JsonResponseModel response) {
                                edt_AddMember.setText("");
                                Toast.makeText(context, R.string.userNoDB, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, "No se pueden añadir más de 10 miembros", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.role_noselected, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.email_NoMatch, Toast.LENGTH_LONG).show();
            }
        } else if ( view.getId() == R.id.btn_AGAAdd ){
            //Revisar elementos no nulos y si no nulos insertar en BBDD
            Log.d("DEBUGME", userEmail + " " + userPass + " " +  edt_AGAGroupName.getText().toString() + " " +
                    edt_AGADescription.getText().toString() + " " +  currencySelection);
            if (!edt_AGAGroupName.getText().toString().equals("") &&
                    !edt_AGADescription.getText().toString().equals("") &&
                    currencySelection != null){
                    webApiRequest.addGroup(userEmail, userPass, edt_AGAGroupName.getText().toString(),
                            edt_AGADescription.getText().toString(), currencySelection, members, new WebApiRequest.WebApiRequestJsonResponseListener() {
                        @Override
                        public void onSuccess(JsonResponseModel response) {
                            Toast.makeText(context, "Grupo creado correctamente", Toast.LENGTH_LONG).show();

                            //Volvemos al login activity
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(JsonResponseModel response) {
                            Toast.makeText(context, response.getId(), Toast.LENGTH_LONG).show();
                        }
                });
            } else {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_LONG).show();
            }
        } //else if ( view.getId() == R.id.btn_AGACancel ){
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(getIntent());
//            overridePendingTransition(0, 0);
//        }

    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        MembersAdapter listAdapter = (MembersAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}