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
import android.widget.Toast;

import com.example.dam_proyecto_final.ui.LoginActivity;
import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.ui.home.homeui.profileui.MyDataActivity;
import com.example.dam_proyecto_final.ui.home.homeui.profileui.PasswordChangeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private String userEmail ="vacio", userPass ="vacio",name="";

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = view.getContext();

        LinearLayout lytprfmydata = view.findViewById(R.id.lytmydata);
        lytprfmydata.setOnClickListener(this);
        LinearLayout lytchangepsw = view.findViewById(R.id.lytchangepsw);
        lytchangepsw.setOnClickListener(this);
        LinearLayout lytsignoff = view.findViewById(R.id.lytsignoff);
        lytsignoff.setOnClickListener(this);

        readSharedPreferences();

        TextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(name);

        return view;
    }

    private void readSharedPreferences(){
        SharedPreferences preferencias;
        preferencias = getActivity().getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email","vacio");
        userPass = preferencias.getString("pass","vacio");
        name= preferencias.getString("name","");
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.lytmydata){
            Intent intentMyData = new Intent(context, MyDataActivity.class);
            intentMyData.putExtra("userEmail", userEmail);
            intentMyData.putExtra("name", name);
            startActivity(intentMyData);
        } else if (view.getId() == R.id.lytchangepsw){
            GoogleSignInAccount accountPass = GoogleSignIn.getLastSignedInAccount(context);
            if (accountPass != null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Cuenta de google iniciada, no hay contraseña que cambiar"
                        , Toast.LENGTH_LONG).show();
            } else {
                Intent intentPass = new Intent(context, PasswordChangeActivity.class);
                intentPass.putExtra("userEmail", userEmail);
                intentPass.putExtra("userPass", userPass);
                startActivity(intentPass);
            }
        } else if (view.getId() == R.id.lytsignoff){
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
            SharedPreferences preferences = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
            preferences.edit().remove("email").apply();
            preferences.edit().remove("pass").apply();

            //Volvemos al login activity
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}