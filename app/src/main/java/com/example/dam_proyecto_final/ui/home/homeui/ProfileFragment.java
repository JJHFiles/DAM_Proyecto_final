package com.example.dam_proyecto_final.ui.home.homeui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dam_proyecto_final.ui.LoginActivity;
import com.example.dam_proyecto_final.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private String userEmail ="vacio", userPass ="vacio",name="vacio";

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = view.getContext();

        lytprfmydata = view.findViewById(R.id.lytmydata);
        lytchangepsw = view.findViewById(R.id.lytchangepsw);
        lytsignoff = view.findViewById(R.id.lytsignoff);
        lytsignoff.setOnClickListener(this);

        tvName= view.findViewById(R.id.tvName);
        tvName.setText(name);
        // Pruebas JJ


        return view;
    }

    private void readSharedPreferences(){
        SharedPreferences preferencias;
        preferencias = getActivity().getSharedPreferences("savedData", Context.MODE_PRIVATE);
        email= preferencias.getString("email","vacio");
        pass= preferencias.getString("pass","vacio");
        name= preferencias.getString("name","vacio");
    }

    @Override
    public void onClick(View view) {
        LinearLayout lyt = (LinearLayout)view;
        switch (lyt.getId()){
            case R.id.lytmydata:
                //
                break;
            case R.id.lytchangepsw:
                //
                break;
            case R.id.lytsignoff:
                //Si tenemos login de google activo
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
                if (account != null) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();

                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
                    mGoogleSignInClient.signOut();
                }
                //Si tenemos login por sharedpreferences
                SharedPreferences preferencias = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
                preferencias.edit().clear().apply();

                //TODO Borrar datos sqlite


                //Volvemos al login activity
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
        }
    }
}