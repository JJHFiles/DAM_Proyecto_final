package com.example.dam_proyecto_final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_proyecto_final.home.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    private Button btnsignGoogle, btnSignin;

    private static final int RC_SIGN_IN = 1, C_LOGIN_STR = 1;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText edtuserEmail, edtpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Chequea si el usuario ya existe en el dispositivo
        checkSharedPreferences();

        Log.d("DEBUGME ", "metodo onCreate");

        //Instanciamos botón de login y establecemos el listener
        btnsignGoogle = findViewById(R.id.btnSignGoogle);
        btnsignGoogle.setOnClickListener(this);
        btnSignin = findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(this);
/*
        txtvUserEmail = findViewById(R.id.txtvUserEmail);
        txtvPass = findViewById(R.id.txtvPass);

        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtPass = findViewById(R.id.edtPass);
*/
        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Instanciamos campos de entrada y listener
        edtuserEmail = findViewById(R.id.edtUserEmail);
        edtuserEmail.setOnFocusChangeListener(this);
        edtpass = findViewById(R.id.edtPass);
        edtpass.setOnFocusChangeListener(this);

        //DEBUG
//        txtvdebug = findViewById(R.id.txtvdebug);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        Log.d("DEBUGME ", "metodo on start");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            //   Toast.makeText(this, account.getEmail(), Toast.LENGTH_LONG).show();
        } else {
            //   Toast.makeText(this, "No se ha iniciado", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                //Toast.makeText(this, "Iniciado", Toast.LENGTH_LONG).show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("DEBUGME ", "Google sign in failed", e);
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                //txtvdebug.setText(e.getMessage());
            }
        }
    }

    //Método de click que invoca al login
    @Override
    public void onClick(View view) {
        //Si el bóton es el de login
        switch (view.getId()) {
            case R.id.btnSignGoogle:
                googleSignIn();
                break;

            case R.id.btnSignin:
                        if (Patterns.EMAIL_ADDRESS.matcher(edtuserEmail.getText().toString()).matches()
                        && !edtpass.getText().toString().contains(getResources().getString(R.string.edtpass_text))
                        && edtpass.getText().toString().length() >= getResources().getInteger(R.integer.min_pass_length)) //4
                {
                    createSharedPreferences();
                    signIn();
                }
                break;

            case R.id.btnSignup:
                // Comprobar si existe el usuaro
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        EditText edt = (EditText) view;
        //  Toast.makeText(this, String.valueOf(edt.getId()), Toast.LENGTH_LONG).show();
        switch (edt.getId()) {
            case R.id.edtUserEmail:
                if (edt.getText().toString().equals(getResources().getString(R.string.edtuseremail_text))) {
                    edt.setText("");
                }
                break;
            case R.id.edtPass:
                edt.setText(null);
                break;
        }
    }

    // Chequea si ya existe el usuario creado
    private void checkSharedPreferences() {
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        if (preferencias.getString("email", "vacio").equals("vacio")) {
            // TODO leer string de resources o eliminar el toast
            Toast.makeText(this, "Usuario NO registrado en el dispositivo", Toast.LENGTH_LONG).show();
        } else {
            signIn();
        }
    }

    private void createSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("savedData", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", edtuserEmail.getText().toString());
        editor.putString("pass", edtpass.getText().toString());
        editor.commit();
    }

    // Utilidad de desarrollo, solo para tester
    private void deleteAllSharedPreferences() {
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        preferencias.edit().clear().commit();
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión con Google
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión usuarios No-Google
    private void signIn() {
        Intent signInIntent = new Intent(getApplicationContext(), HomeActivity.class);
        //  createSharedPreferences();
        startActivity(signInIntent);
    }

}//End Class