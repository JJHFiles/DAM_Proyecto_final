package com.example.dam_proyecto_final.home.homeui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dam_proyecto_final.model.ActivityModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.web_api.WebApiRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String email = "vacio";
    private String pass = "vacio";

    private ListView lv;
    private Context context;

    private WebApiRequest webApiRequest;

    public ActivityFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ActivityFragment newInstance(String param1, String param2) {
        ActivityFragment fragment = new ActivityFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        lv = view.findViewById(R.id.lv);
        context = view.getContext();
        SharedPreferences preferencias = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
        String userEmail = preferencias.getString("email", "vacio");


        getActivityByEmail(userEmail);
        return view;
    }


    public void getActivityByEmail(String email) {
        webApiRequest = new WebApiRequest(context);
        webApiRequest.getActivityByEmail(email, new WebApiRequest.WebApiRequestJsonObjectArrayListener() {
            @Override
            public void onSuccess(JsonResponseModel response, List<?> data) {
                Log.d("DEBUGME", response.getId() + " " + response.getMessage());

                fillListView((ArrayList<ActivityModel>) data);
            }

            @Override
            public void onError(JsonResponseModel response) {
                if (response.getId() == -253) {
                    //Si es -252 es que el usuario no tiene actividades
                    Toast.makeText(context, "Error " + response.getId() + " Sin actividades", Toast.LENGTH_LONG);


                } else {
                    //Si no ha podido ser cualquier error
                    Toast.makeText(context, "Error " + response.getId(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void fillListView(ArrayList<ActivityModel> al) {

        ArrayList<String> arr = new ArrayList<>();
        for (int x = 0; x < al.size(); x++) {
            arr.add(""
                    + "#" + (x + 1)
              //      + "\nID Actividad: " + al.get(x).getIdactivity()
                    + "\nActividad: " + al.get(x).getAction()
                    + "\nNombre: " + al.get(x).getName()
                    + "\nFecha: " + al.get(x).getDate_activity()
                    + "\nDescripcion: " + al.get(x).getDescription()
                    + "\nCreador: " + al.get(x).getEmail());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.activity_fragmet_listview, arr);
        lv.setAdapter(arrayAdapter);
/*
        AdapterView.OnItemClickListener lvClick = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(context, "Seleccionado elemento: "+position, Toast.LENGTH_LONG).show();
            }
        };
        lv.setOnItemClickListener(lvClick);
*/
    }
}