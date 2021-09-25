package com.example.dam_proyecto_final.home.homeui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dam_proyecto_final.Model.Group;
import com.example.dam_proyecto_final.Model.JsonResponse;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.home.homeui.groupui.GroupAdapter;
import com.example.dam_proyecto_final.home.homeui.groupui.GroupAddActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView txtv;

    private String email="vacio";
    private String pass="vacio";

    private Button btn_FGEAddGroup;
    private TextView txtv_FGEmptyTitle;
    private TextView txtv_FGEmptyDescription;
    private ListView lstv_FGGroups;
    private ImageView imgv_FGEmptyAnimation;

    private Context context;

    private WebApiRequest webApiRequest;
    private String userEmail;
    private String userPass;

    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        readSharedPreferences();
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
        txtv_FGEmptyTitle = view.findViewById(R.id.txtv_FGEmptyTitle);
        txtv_FGEmptyDescription = view.findViewById(R.id.txtv_FGEmptyDescription);
        lstv_FGGroups = view.findViewById(R.id.lstv_FGGroups);
        imgv_FGEmptyAnimation = view.findViewById(R.id.imgv_FGEmptyAnimation);

        //Cogemos el usuario/contraseña para las consultas
        SharedPreferences preferencias = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", "vacio");
        userPass = preferencias.getString("pass", "vacio");

        //Invocamos los grupos
        webApiRequest = new WebApiRequest(context);
        webApiRequest.getGroupsByEmail(userEmail, userPass, new WebApiRequest.WebApiRequestJsonObjectArrayListener() {
            @Override
            public void onSuccess(JsonResponse response, List<?> data) {
                Log.d("DEBUGME", response.getId() + " " + response.getMessage());
                ArrayList<Group> groups = (ArrayList<Group>) data;
//                for (Group g: groups){
//                    Log.d("DEBUGME groups", g.getDescripción());
//                }
                //lstv_FGGroups
                GroupAdapter groupAdapter = new GroupAdapter(context, groups);
                lstv_FGGroups.setAdapter(groupAdapter);
                imgv_FGEmptyAnimation.setVisibility(View.INVISIBLE);
                lstv_FGGroups.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(JsonResponse response) {
                if (response.getId() == -253){
                    //Si es -252 es que el usuario no tiene grupos
                    //Ponemos visibles los textos de empty
                    txtv_FGEmptyTitle.setVisibility(View.VISIBLE);
                    txtv_FGEmptyDescription.setVisibility(View.VISIBLE);

                } else {
                    //Si no ha podido ser cualquier error
                    Toast.makeText(context, "Error " + response.getId(), Toast.LENGTH_LONG);
                }
            }
        });

        return view;
    }

    private void readSharedPreferences(){

        SharedPreferences preferencias;
        preferencias = getActivity().getSharedPreferences("savedData", Context.MODE_PRIVATE);
        email= preferencias.getString("email","vacio");
        pass= preferencias.getString("pass","vacio");
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), GroupAddActivity.class);
        startActivity(intent);

    }
}