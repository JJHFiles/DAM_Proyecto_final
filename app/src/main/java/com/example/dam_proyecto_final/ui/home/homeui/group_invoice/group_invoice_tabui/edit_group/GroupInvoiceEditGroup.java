package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui.edit_group;

import android.content.Context;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.model.MemberModel;
import com.example.dam_proyecto_final.model.SharedModel;
import com.example.dam_proyecto_final.ui.home.HomeActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class GroupInvoiceEditGroup extends AppCompatActivity implements View.OnClickListener {

    private ListView lstv_Members;
    private ArrayList<MemberModel> membersOld, membersLis, membersAdd, membersUpd, membersDel;
    private TextInputEditText edt_AGIEG_AddMember;
    private TextInputEditText edt_AGIEG_GroupName;
    private TextInputEditText edt_AGIEG_Description;
    private ImageButton imgb_AGIEG_AddMember;
    private GroupInvoiceEditGroupMemberAdapter membersAdapter;
    private AutoCompleteTextView dd_AGIEG_Currency;
    private AutoCompleteTextView dd_AGIEG_Role;
    private Button btn_AGIEG_Add;
    private ArrayAdapter rolesAdapter;
    private String roleSelection;
    private String currencySelection;
    private String userEmail, groupName, currency, groupDescription;
    private int idGroup;
    private String userPass;
    private WebApiRequest webApiRequest;
    private Context context;
    ViewGroup.LayoutParams params;
    //private ArrayList<GroupModel> groupModels;
    // private GroupModel groupModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_edit_group);

        context = this;
        webApiRequest = new WebApiRequest(context);

        //Cogemos la información de grupo obtenida del GroupFragment
        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            idGroup = parametros.getInt("idGroup", -1);
            groupName = parametros.getString("groupName", "vacio");
            userEmail = parametros.getString("userEmail", "vacio");
            userPass = parametros.getString("userPass", "vacio");
            currency = parametros.getString("currency", "vacioCurrency");


//            Toast.makeText(context, "idGroup " + idGroup, Toast.LENGTH_LONG).show();
            Log.d("DEBUGME", "GroupInvoiceTab: grupo " + idGroup);

        } else {
            Log.d("DEBUGME", "GroupInvoiceTab: ERROR GRAVE idGroup = null");
//            Toast.makeText(context, "ERROR GRAVE idGroup = null", Toast.LENGTH_LONG).show();
        }

        //Editamos la barra superior con nombre y botón back
        getSupportActionBar().setTitle("Editar grupo " + groupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        membersLis = new ArrayList<MemberModel>(); // Miembros que se listan en la GUI. Los introducidos manualmente (nuevos o Add) y los que ya estaban en el grupo (Old).
        membersOld = new ArrayList<MemberModel>(); // Miembros recibidos del grupo antes de editar ,necesario para no volverlos a introducir en el grupo.
        membersAdd = new ArrayList<MemberModel>(); // Miembros nuevos que se añadirán al grupo.
        membersUpd = new ArrayList<MemberModel>(); // Miembros que se han actualizado ya que se ha cambiado su rol.
        membersDel = new ArrayList<MemberModel>(); // Miembros recibidos del grupo que han sido seleccionados para ser eliminados del grupo.

        // Carga los usuarios del grupo actual y los muestra en una lista
        getGroupAndShared();

        //Asignamos lista a DropDowns
        String[] currencys = {"EUR", "USD", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "BRL"};

        ArrayAdapter currencyAdapter =
                new ArrayAdapter(this, R.layout.activity_group_add_dropdown_item, currencys);

        dd_AGIEG_Currency = findViewById(R.id.dd_AGIEG_Currency);
        dd_AGIEG_Currency.setText(currency);
        dd_AGIEG_Currency.setAdapter(currencyAdapter);

        // se obtiene el currency actual del grupo.
        for (int x = 0; x < dd_AGIEG_Currency.getAdapter().getCount(); x++) {
            if (dd_AGIEG_Currency.getAdapter().getItem(x).toString().equals(currency)) {
                dd_AGIEG_Currency.setListSelection(x);
                currencySelection = dd_AGIEG_Currency.getAdapter().getItem(x).toString();
            }
        }

        dd_AGIEG_Currency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                currencySelection = (String) parent.getItemAtPosition(position);
            }
        });

        String[] roles = {
                getString(R.string.role_admin),
                getString(R.string.role_editor),
                getString(R.string.role_reader)
        };

        rolesAdapter = new ArrayAdapter(this, R.layout.activity_group_add_dropdown_item, roles);
        dd_AGIEG_Role = findViewById(R.id.dd_AGIEG_Role);
        dd_AGIEG_Role.setAdapter(rolesAdapter);
        dd_AGIEG_Role.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                roleSelection = (String) parent.getItemAtPosition(position);
                //  Toast.makeText(context, "Role:"+roleSelection, Toast.LENGTH_LONG).show();

            }
        });

        //EditText de inclusión de miembro
        edt_AGIEG_AddMember = findViewById(R.id.edt_AGIEG_AddMember);
        imgb_AGIEG_AddMember = findViewById(R.id.imgb_AGIEG_AddMember);
        imgb_AGIEG_AddMember.setOnClickListener(this);
        edt_AGIEG_GroupName = findViewById(R.id.edt_AGIEG_GroupName);
        edt_AGIEG_Description = findViewById(R.id.edt_AGIEG_Description);

        edt_AGIEG_GroupName.setText(groupName);


        //Buttons
        btn_AGIEG_Add = findViewById(R.id.btn_AGIEG_Add);
        btn_AGIEG_Add.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        //TODO revisar que el elemento no esté ya en la lista
        if (view.getId() == R.id.imgb_AGIEG_AddMember) {
            //Comprobamos que el email y el rol no esten vacios y que el email se ajuste formato. Tambien que el miembro no esté ya en la lista
            if (Patterns.EMAIL_ADDRESS.matcher(edt_AGIEG_AddMember.getText().toString()).matches()) {
                if (!edt_AGIEG_AddMember.getText().toString().equals("") && roleSelection != null) {
                    //Comprobamos que no haya ya 10 usuario en la lista (máximo 10 usuarios por grupo)
                    if (membersLis.size() < 10) {
                        //Comprobamos que el mail no esté ya en la lista
                        Boolean memberExist = false;
                        for (MemberModel m : membersLis) {
                            if (m.getEmail().equals(edt_AGIEG_AddMember.getText().toString())) {
                                memberExist = true;

                                int role = 2;
                                if (roleSelection.toString().equals(getString(R.string.role_admin))) {
                                    role = 0;
                                } else if (roleSelection.toString().equals(getString(R.string.role_editor))) {
                                    role = 1;
                                } else if (roleSelection.toString().equals(getString(R.string.role_reader))) {
                                    role = 2;
                                }
                                // Se agrega el usuario para actualizar su rol en el grupo
                                membersUpd.add(new MemberModel(edt_AGIEG_AddMember.getText().toString(), role));

                                m.setRole(role);

                                // Vuelve a cargar el adaptador y lo refresca actualizado
                                membersAdapter.notifyDataSetChanged();
                            }
                        }
                        //Si no existe ok si existe toast
                        if (!memberExist) {
                            int dec = 0;
                            if (membersOld.size() > 1) {
                                dec = -1;
                            }
                            //Controlamos que no se agregue un usuario que ya estaba en la lista original membersOld
                            for (int x = 0; x < membersOld.size()+dec; x++) {
                                if (!edt_AGIEG_AddMember.getText().toString().equals(membersOld.get(x).getEmail())) {

                                    // Si el usuario está pendiente de eliminarse del grupo ya no eliminará
                                    for (int y = 0; x < membersDel.size(); y++) {
                                        if (membersDel.get(y).getEmail().equals(edt_AGIEG_AddMember.getText().toString())) {
                                            membersDel.remove(membersDel.get(y));
                                        }
                                    }

                                    //Comprobar si el mail existe en BD
                                    webApiRequest.getIfEmailExist(edt_AGIEG_AddMember.getText().toString(), new WebApiRequest.WebApiRequestJsonResponseListener() {
                                        @Override
                                        public void onSuccess(JsonResponseModel response) {
                                            int role = 2;
                                            if (roleSelection.toString().equals(getString(R.string.role_admin))) {
                                                role = 0;
                                            } else if (roleSelection.toString().equals(getString(R.string.role_editor))) {
                                                role = 1;
                                            } else if (roleSelection.toString().equals(getString(R.string.role_reader))) {
                                                role = 2;
                                            }


                                            membersLis.add(new MemberModel(edt_AGIEG_AddMember.getText().toString(), role));

                                            // Usuarios que se añadirán al grupo
                                            membersAdd.add(new MemberModel(edt_AGIEG_AddMember.getText().toString(), role));

                                            membersAdapter.notifyDataSetChanged();
                                            edt_AGIEG_AddMember.setText("");
                                            setListViewHeightBasedOnChildren(lstv_Members);
                                        }

                                        @Override
                                        public void onError(JsonResponseModel response) {
                                            edt_AGIEG_AddMember.setText("");
                                            Toast.makeText(context, R.string.userNoDB, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(this, "No se puede agregar un usuario que ya estaba en el grupo con anterioridad", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(this, "Ya se ha incluido al miembro en la lista", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se pueden añadir más de 10 miembros", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.role_noselected, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.email_NoMatch, Toast.LENGTH_LONG).show();
            }

            /* TODO IMPORTANTE comprobar que algún usuario del grupo es administrador nates de grabar en BD
               si no es así no dejar hacer update en el grupo hasta que un usuario tenga rol admin
             */
        } else if (view.getId() == R.id.btn_AGIEG_Add) {
            //Revisar elementos no nulos y si no nulos insertar en BBDD
            Log.d("DEBUGME", userEmail + " " + userPass + " " + edt_AGIEG_GroupName.getText().toString() + " " +
                    edt_AGIEG_Description.getText().toString() + " " + currencySelection);
            if (!edt_AGIEG_GroupName.getText().toString().equals("") &&
                    !edt_AGIEG_Description.getText().toString().equals("") &&
                    currencySelection != null) {
                groupDescription = edt_AGIEG_Description.getText().toString() + "";

                webApiRequest.updateGroup(
                        userEmail,
                        userPass,
                        idGroup + "",/* Cast a String*/
                        edt_AGIEG_GroupName.getText().toString(),
                        edt_AGIEG_Description.getText().toString(),
                        currencySelection,
                        membersAdd,
                        membersUpd,
                        membersDel,
                        new WebApiRequest.WebApiRequestJsonResponseListener() {
                            @Override
                            public void onSuccess(JsonResponseModel response) {
                                Toast.makeText(context, "Grupo actualizado correctamente", Toast.LENGTH_LONG).show();

                                //Volvemos al login activity
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(JsonResponseModel response) {
                                Toast.makeText(context, response.getId() + "", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_LONG).show();
            }
        } //else if ( view.getId() == R.id.btn_AGIEG_Cancel ){
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(getIntent());
//            overridePendingTransition(0, 0);
//        }

    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        GroupInvoiceEditGroupMemberAdapter listAdapter = (GroupInvoiceEditGroupMemberAdapter) listView.getAdapter();
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


        params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void getGroupAndShared() {
        webApiRequest.getGroupAndShared
                (userEmail, userPass, idGroup, new WebApiRequest.WebApiRequestJsonObjectArrayListenerV2() {
                    @Override
                    public void onSuccess(JsonResponseModel response, GroupModel group, List<?> shared) {
                        Log.d("DEBUGME", response.getId() + " " + response.getMessage());

                        edt_AGIEG_Description.setText(group.getDescription());

                        ArrayList sharedModel = (ArrayList) shared;

                        for (int x = 0; x < sharedModel.size(); x++) {
                            membersLis.add(new MemberModel(
                                    ((SharedModel) (sharedModel.get(x))).getEmail(),
                                    ((SharedModel) (sharedModel.get(x))).getRole())
                            );
                            // Para controlar que usuarios ya estaban en el grupo y no volverlos a introducir
                            membersOld.add(new MemberModel(
                                    ((SharedModel) (sharedModel.get(x))).getEmail(),
                                    ((SharedModel) (sharedModel.get(x))).getRole())
                            );
                        }
                        //ListView de miembros
                        lstv_Members = findViewById(R.id.lstv_AGIEG_Members);
                        //  members = new ArrayList<MemberModel>();
                        membersAdapter = new GroupInvoiceEditGroupMemberAdapter(getApplicationContext(), membersLis, membersAdd, membersUpd, membersDel, lstv_Members, userEmail);
                        lstv_Members.setAdapter(membersAdapter);
                        // Vuelve a cargar el adaptador y lo refresca actualizado
                        membersAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(lstv_Members);

                    }

                    @Override
                    public void onError(JsonResponseModel response) {
                        if (response.getId() == -252) {
                            Toast.makeText(context, "Error " + response.getId(), Toast.LENGTH_LONG).show();

                        } else {
                            //Si no ha podido ser cualquier error
                            Toast.makeText(context, "Error " + response.getId(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    // Método que controla el click en la flecha back del ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}