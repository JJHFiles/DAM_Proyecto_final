package com.example.dam_proyecto_final.ui.home.homeui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.GroupInvoiceEmptyActivity;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.GroupInvoiceTab;
import com.example.dam_proyecto_final.ui.home.homeui.groupui.GroupAdapter;
import com.example.dam_proyecto_final.ui.home.homeui.groupui.GroupAddActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment implements View.OnClickListener {

    private String email = "vacioEmail";
    private String pass = "vacioPass";

    private Button btn_FGEAddGroup;
    private TextView txtv_FGEmptyTitle;
    private TextView txtv_FGEmptyDescription;
    private ListView lstv_FGGroups;
    private ImageView imgv_FGEmptyAnimation;

    private Context context;

    private WebApiRequest webApiRequest;
    private String userEmail, userPass, idGroup;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        context = view.getContext();

        //Instanciamos vistas
        btn_FGEAddGroup = view.findViewById(R.id.btn_FGAddGroup);
        btn_FGEAddGroup.setOnClickListener(this);
        txtv_FGEmptyTitle = view.findViewById(R.id.txtv_EmptyTitle);
        txtv_FGEmptyDescription = view.findViewById(R.id.txtv_EmptyDescription);
        lstv_FGGroups = view.findViewById(R.id.lstv_Activities);
        imgv_FGEmptyAnimation = view.findViewById(R.id.imgv_EmptyAnimation);

        //Cogemos el usuario/contraseña para las consultas
        SharedPreferences preferencias = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", "vacioMail");
        userPass = preferencias.getString("pass", "vacioPassword");

        //Invocamos los grupos
        webApiRequest = new WebApiRequest(context);
        webApiRequest.getGroupsByEmail
                (userEmail, userPass, new WebApiRequest.WebApiRequestJsonObjectArrayListener() {
            @Override
            public void onSuccess(JsonResponseModel response, List<?> data) {

                Log.d("DEBUGME", response.getId() + " " + response.getMessage());

                ArrayList<GroupModel> groupModels = (ArrayList<GroupModel>) data;
                GroupAdapter groupAdapter = new GroupAdapter(context, groupModels);
                lstv_FGGroups.setAdapter(groupAdapter);
                imgv_FGEmptyAnimation.setVisibility(View.INVISIBLE);
                lstv_FGGroups.setVisibility(View.VISIBLE);

                AdapterView.OnItemClickListener lvClick = new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        idGroup = groupModels.get(position).getId() + "";
                        isInvoiceByGroup(idGroup, groupModels, position);

                    }
                };
                lstv_FGGroups.setOnItemClickListener(lvClick);
            }

            @Override
            public void onError(JsonResponseModel response) {
                if (response.getId() == -252) {
                    //Si es -252 es que el usuario no tiene grupos
                    //Ponemos visibles los textos de empty
                    imgv_FGEmptyAnimation.setVisibility(View.VISIBLE);
                    txtv_FGEmptyTitle.setVisibility(View.VISIBLE);
                    txtv_FGEmptyDescription.setVisibility(View.VISIBLE);


                } else {
                    //Si no ha podido ser cualquier error
                   // Toast.makeText(context, "Error " + response.getId(), Toast.LENGTH_LONG);
                    Log.d("DEBUGME","ERROR: " + response.getId());

                }
            }
        });

        return view;
    }

    //Método on click del botón añadir grupo
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), GroupAddActivity.class);
        startActivity(intent);

    }

    // Comprueba si existe el usuario en la bd
    public void isInvoiceByGroup(String groupId, ArrayList<GroupModel> groupModel, int position) {
        webApiRequest.isInvoiceByGroup(userEmail, userPass, groupId, new WebApiRequest.WebApiRequestJsonObjectListener() {
            @Override
            public void onSuccess(int id, String message) {
                if (id == 222) {
                    Log.d("DEBUGME", "Group without invoices:" + groupId + ", " + message);

                    Intent intent = new Intent(context, GroupInvoiceTab.class);
                    intent.putExtra("groupModel", groupModel.get(position));
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userPass", userPass);
                    startActivity(intent);

                } else if (id == 223) {
                    Log.d("DEBUGME", "Group without invoices: " + groupId + ", id:" + id);

                    Intent intent = new Intent(context, GroupInvoiceEmptyActivity.class);
                    intent.putExtra("groupModel", groupModel.get(position));
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userPass", userPass);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int id, String message) {
                Log.d("DEBUGME", "GroupFragment onError: " + id + " " + message);
              //  Toast.makeText(context, "Error al inicar sesión. Codigo de error: " + id, Toast.LENGTH_LONG).show();
            }
        });
    }

}