package com.example.dam_proyecto_final.ui.home.homeui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.ui.home.homeui.groupui.ActivityModelAdapter;
import com.example.dam_proyecto_final.model.ActivityModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.web_api.WebApiRequest;

import java.util.ArrayList;
import java.util.List;


public class ActivityFragment extends Fragment {


    private String email = "vacio";
    private String pass = "vacio";

    private ListView lv;
    private Context context;

    private WebApiRequest webApiRequest;

    private TextView txtv_EmptyTitle,txtv_EmptyDescription;
    private ImageView imgv_EmptyAnimation;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        lv = view.findViewById(R.id.lv);
        context = view.getContext();

        txtv_EmptyDescription=view.findViewById(R.id.txtv_EmptyDescription);
        txtv_EmptyTitle=view.findViewById(R.id.txtv_EmptyTitle);
        imgv_EmptyAnimation=view.findViewById(R.id.imgv_EmptyAnimation);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferencias = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
        String userEmail = preferencias.getString("email", "vacio1");
        String userPass = preferencias.getString("pass", "vacio2");


        getActivityByEmail(userEmail, userPass);

    }

    public void getActivityByEmail(String email, String userPass) {

        webApiRequest = new WebApiRequest(context);
        // TODO editar peticion para que se ajuste al nuevo activity model
        webApiRequest.getActivityByEmail(email, userPass, new WebApiRequest.WebApiRequestJsonObjectArrayListener() {
            @Override
            public void onSuccess(JsonResponseModel response, List<?> data) {
                Log.d("DEBUGME", response.getId() + " " + response.getMessage());

                txtv_EmptyDescription.setVisibility(View.INVISIBLE);
                txtv_EmptyTitle.setVisibility(View.INVISIBLE);
                imgv_EmptyAnimation.setVisibility(View.INVISIBLE);
                lv.setVisibility(View.VISIBLE);


                lv.setAdapter(new ActivityModelAdapter(getContext(), (ArrayList<ActivityModel>)data));

            }

            @Override
            public void onError(JsonResponseModel response) {
                txtv_EmptyDescription.setVisibility(View.VISIBLE);
                txtv_EmptyTitle.setVisibility(View.VISIBLE);
                imgv_EmptyAnimation.setVisibility(View.VISIBLE);

                lv.setVisibility(View.INVISIBLE);

                if (response.getId() == -253) {
                    //Si es -252 es que el usuario no tiene actividades
                    Log.d("DEBUGME", response.getId() + " " + response.getMessage());
//                    Toast.makeText(context, "Error " + response.getId() + " Sin actividades", Toast.LENGTH_LONG);


                } else {
                    //Si no ha podido ser cualquier error
                    Log.d("DEBUGME", response.getId() + " " + response.getMessage());
//                    Toast.makeText(context, "Error " + response.getId(), Toast.LENGTH_LONG);
                }
            }
        });

    }

}