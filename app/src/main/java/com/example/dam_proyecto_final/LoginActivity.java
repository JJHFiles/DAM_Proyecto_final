package com.example.dam_proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    SignInButton btnsign;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("DEBUGME ", "metodo onCreate");

        //Instanciamos botón de login y establecemos el listener
        btnsign = findViewById(R.id.btnSign);
        btnsign.setOnClickListener(this);


        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
            case R.id.btnSign:
                signIn();
                break;
        }
    }

    //Método que registra la finalización de otra actividad (cuando cierra el login)
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
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("DEBUGME ", "Google sign in failed", e);
            }
        }
    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        Log.d("DEBUGME ", "handleSignInResult");
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            //updateUI(account);
//            Toast.makeText(this, account.getEmail(), Toast.LENGTH_LONG);
//            Log.d("DEBUGME ", "login handler correcto?");
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.d("DEBUGME", "signInResult:failed code=" + e.getStatusCode());
//            //updateUI(null);
//            Toast.makeText(this, "Error de login", Toast.LENGTH_LONG);
//        }
//    }




}//End Class