package com.example.dam_proyecto_final.home.homeui;

import android.app.Activity;
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

import com.example.dam_proyecto_final.LoginActivity;
import com.example.dam_proyecto_final.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private LinearLayout lytprfmydata;
    private LinearLayout lytchangepsw;
    private LinearLayout lytsignoff;

    private TextView tvName;
    private String email="vacio",pass="vacio",name="vacio";

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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