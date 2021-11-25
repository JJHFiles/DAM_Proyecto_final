package com.example.dam_proyecto_final.ui.home.homeui.groupui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.model.MemberModel;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.ui.home.HomeActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class GroupAddActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ListView lstv_Members;
    private ArrayList<MemberModel> members;
    private TextInputEditText edt_AddMember;
    private TextInputEditText edt_AGAGroupName;
    private TextInputEditText edt_AGADescription;
    private MembersAdapter membersAdapter;
    private String roleSelection;
    private String currencySelection;
    private String userEmail;
    private String userPass;
    private WebApiRequest webApiRequest;
    private Context context;
    private String typeSelection;
    private AutoCompleteTextView dd_AGACurrency;
    Button btn_AGAAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);

        context = this;
        webApiRequest = new WebApiRequest(context);

        //Editamos la barra superior con nombre y botón back
        Objects.requireNonNull(getSupportActionBar()).setTitle("Crear grupo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Cogemos el usuario/contraseña para las consultas
        SharedPreferences preferencias = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", "vacio");
        userPass = preferencias.getString("pass", "vacio");

        //Asignamos lista a DropDowns
        String[] currencys = {"EUR", "USD", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "BRL"};
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(this, R.layout.activity_group_add_dropdown_item, currencys);
        dd_AGACurrency = findViewById(R.id.dd_AGACurrency);
        dd_AGACurrency.setAdapter(currencyAdapter);
        dd_AGACurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                currencySelection = (String) parent.getItemAtPosition(position);
                typeSelection = parent.getItemAtPosition(position).toString();
                btn_AGAAdd.setEnabled(checkFields());
            }
        });
        dd_AGACurrency.addTextChangedListener(this);

        String[] roles = {getString(R.string.role_admin), getString(R.string.role_editor), getString(R.string.role_reader)};
        ArrayAdapter<String> rolesAdapter = new ArrayAdapter<>(this, R.layout.activity_group_add_dropdown_item, roles);
        AutoCompleteTextView dd_AGARole = findViewById(R.id.dd_AGARole);
        dd_AGARole.setAdapter(rolesAdapter);
        dd_AGARole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                roleSelection = (String) parent.getItemAtPosition(position);
            }
        });


        //EditText de inclusión de miembro
        edt_AddMember = findViewById(R.id.edt_AddMember);
        ImageButton imgb_AddMember = findViewById(R.id.imgb_AGAAddMember);
        imgb_AddMember.setOnClickListener(this);
        edt_AGAGroupName = findViewById(R.id.edt_AGAGroupName);
        edt_AGAGroupName.addTextChangedListener(this);
        edt_AGADescription = findViewById(R.id.edt_AGADescription);
        edt_AGADescription.addTextChangedListener(this);

        //ListView de miembros
        lstv_Members = findViewById(R.id.lstv_AGAMembers);
        members = new ArrayList<>();
        membersAdapter = new MembersAdapter(this, members, lstv_Members);
        lstv_Members.setAdapter(membersAdapter);

        btn_AGAAdd = findViewById(R.id.btn_AGAAdd);
        btn_AGAAdd.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgb_AGAAddMember){
            //Comprobamos que el email y el rol no esten vacios y que el email se ajuste formato. Tambien que el miembro no esté ya en la lista
            if (Patterns.EMAIL_ADDRESS.matcher(edt_AddMember.getText().toString()).matches()) {
                if (!edt_AddMember.getText().toString().equals("") && roleSelection != null ){
                    //Comprobamos que no haya ya 10 usuario en la lista (máximo 10 usuarios por grupo)
                    if (members.size() < 10) {
                        //Comprobamos que el mail no esté ya en la lista
                        boolean memberExist = false;
                        for (MemberModel m : members) {
                            if (m.getEmail().equals(edt_AddMember.getText().toString())) {
                                memberExist = true;
                            }
                        }
                            //Si no existe ok si existe toast
                        if (!memberExist){
                            //Comprobar si el mail existe en BD
                            webApiRequest.getIfEmailExist(edt_AddMember.getText().toString(), new WebApiRequest.WebApiRequestJsonResponseListener() {
                                @Override
                                public void onSuccess(JsonResponseModel response) {
                                    int role = 2;
                                    if (roleSelection.equals(getString(R.string.role_admin))){
                                        role = 0;
                                    } else if (roleSelection.equals(getString(R.string.role_editor))){
                                        role = 1;
                                    } else if (roleSelection.equals(getString(R.string.role_reader))){
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
                            Toast.makeText(this, getString(R.string.warning_member_on_list), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.warning_member_max), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, getString(R.string.warning_group_created), Toast.LENGTH_LONG).show();

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

    // Métodos para habilitar el botón con el form completo
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    private boolean checkFields() {
        if (edt_AGAGroupName.getText().toString().equals("")) {
            return false;
        }
        if (edt_AGADescription.getText().toString().equals("")) {
            return false;
        }
        if (typeSelection == null || typeSelection.equals("")) {
            return false;
        }

        return true;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (checkFields()) {
            btn_AGAAdd.setEnabled(true);
        } else {
            btn_AGAAdd.setEnabled(false);
        }
    }
}