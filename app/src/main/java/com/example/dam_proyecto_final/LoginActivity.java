package com.example.dam_proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    Button btnsign;
    private static final int RC_SIGN_IN = 1;
    private static final int RC_LOGIN_STR = 1;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText edtuser;
    private EditText edtpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("DEBUGME ", "metodo onCreate");

        //Instanciamos botón de login y establecemos el listener
        btnsign = findViewById(R.id.btnSignGoogle);
        btnsign.setOnClickListener(this);


        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Instanciamos campos de entrada y listener
        edtuser = findViewById(R.id.edtUser);
            edtuser.setOnFocusChangeListener(this);
        edtpass = findViewById(R.id.edtPass);
            edtpass.setOnFocusChangeListener(this);
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        Log.d("DEBUGME ", "metodo on start");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            Toast.makeText(this, account.getEmail(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se ha iniciado", Toast.LENGTH_LONG).show();
        }


    }

    //Método de click que invoca al login
    @Override
    public void onClick(View view) {
        //Si el bóton es el de login
        switch (view.getId()) {
            case R.id.btnSignGoogle:
                signIn();
                break;
        }
    }

    //Método que registra la finalización de otra actividad (cuando cierra el modal login)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("DEBUGME ", "firebaseAuthWithGoogle:" + account.getEmail());
                //firebaseAuthWithGoogle(account.getIdToken());
                //Lógica
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("DEBUGME ", "Google sign in failed", e);
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        EditText edt = (EditText) view;
        Toast.makeText(this, String.valueOf(edt.getId()), Toast.LENGTH_LONG).show();
        switch (edt.getId()){
            case R.id.edtUser:
                if (edt.getText().toString().equals(getResources().getString(R.string.edtuser_text))){
                    edt.setText("");
                }
                break;
            case R.id.edtPass:
                edt.setText("");
                break;
        }
    }



}//End Class