package com.example.dam_proyecto_final.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.ui.home.HomeActivity;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.ui.registry.RegistryActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    private WebApiRequest webApiRequest;
    private Context context;


    private EditText edtUserEmail, edtUserPass;

    private String userEmail = "", userPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Quitar cabecera
        Objects.requireNonNull(getSupportActionBar()).hide();

        //WebApiRequest
        webApiRequest = new WebApiRequest(this);
        context = getApplicationContext();

        Log.d("DEBUGME ", "method onCreate");

        //Instanciamos botones establecemos el listeners
        Button btnsignGoogle = findViewById(R.id.btnSignGoogle);
        btnsignGoogle.setOnClickListener(this);
        Button btnSignIn = findViewById(R.id.btnSignin);
        btnSignIn.setOnClickListener(this);
        LinearLayout llay_SignUp = findViewById(R.id.llay_SignUp);
        llay_SignUp.setOnClickListener(this);


        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Instanciamos campos de entrada y listener
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserEmail.setOnFocusChangeListener(this);
        edtUserPass = findViewById(R.id.edtUserPass);
        edtUserPass.setOnFocusChangeListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("DEBUGME ", "method onStart");

        //Comprobamos si existe previamente un usuario Google logeado
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //Comprobamos previamente si existe un usuario por SharedPreferences Logeado
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", null);

        //Si alguno de los dos no es nulo hay inicio de sesión previo
        if (account != null || userEmail != null) {
            Log.d("DEBUGME ", "LoginActivity onStart: Session started and cached");
            //Iniciamos sesión
            signIn();
        }
    }

    //Método que registra la finalización de otra actividad (cuando cierra el modal login de G)
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


                webApiRequest.userInsertG(account.getEmail(), account.getDisplayName(), account.getId(), new WebApiRequest.WebApiRequestJsonObjectListener() {
                    @Override
                    public void onSuccess(int id, String message) {
                        if (id > 0) {
                            Log.d("DEBUGME", "loginactivity onSucess: " + id + " " + message);

                            //Guardamos el usuario en las SharedPreferences
                            SharedPreferences preferences = getSharedPreferences("savedData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", account.getEmail());
                            editor.putString("name", account.getDisplayName());
                            editor.putString("pass", account.getId());
                            editor.apply();

                            //Iniciamos sesión
                            signIn();
                        } else if (id < 0) {
                            Log.d("DEBUGME", "LoginActivity onSucess: " + id + " " + message);
                           // Toast.makeText(context, "Error starting session. Error code: " + id, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(int id, String message) {
                        Log.d("DEBUGME", "LoginActivity oneError: " + id + " " + message);
                       // Toast.makeText(context, "Error starting session. Error code: " + id, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("DEBUGME ", "Google sign in failed", e);
                Toast.makeText(this, getString(R.string.warning_login_failed_google), Toast.LENGTH_LONG).show();
            }
        }
    }

    //Método de click compuesto por 3 botones: Login no Google, login Google y registro
    @Override
    public void onClick(View view) {
        //Si el bóton es el de login
        switch (view.getId()) {
            case R.id.btnSignGoogle: // loga con cuenta google
                googleSignIn();
                break;

            case R.id.btnSignin: // loga con cuenta No google

                // Si se cumplen formato y tamaño de contraseña
                if (Patterns.EMAIL_ADDRESS.matcher(edtUserEmail.getText().toString()).matches()
                        && !edtUserPass.getText().toString().contains(getResources().getString(R.string.edtpass_text))
                        && edtUserPass.getText().toString().length() >= getResources().getInteger(R.integer.min_pass_length)) //4
                {
                    userEmail = edtUserEmail.getText().toString();
                    userPass = edtUserPass.getText().toString();
                    validateUser(edtUserEmail.getText().toString());
                } else {
                    Toast.makeText(this, getResources().getString(R.string.login_failure), Toast.LENGTH_LONG).show();
                    Log.d("DEBUGME ", getString(R.string.login_failure));

                }
                break;

            case R.id.llay_SignUp: // Entra en el activit de registro de cuenta No google
                Intent registryIntent = new Intent(getApplicationContext(), RegistryActivity.class);
                startActivity(registryIntent);
                break;

        }
    }

    //Metodo de limpieza de los EditText tras hacerlos foco
    @Override
    public void onFocusChange(View view, boolean b) {
        EditText edt = (EditText) view;
        switch (edt.getId()) {
            case R.id.edtUserEmail:
                if (edt.getText().toString().equals(getResources().getString(R.string.edtuseremail_text))) {
                    edt.setText("");
                }
                break;
            case R.id.edtUserPass:
                edt.setText("");
                break;
        }
    }

    // Chequea si ya existe el usuario creado
//    private boolean checkSharedPreferences() {
//        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
//        userEmail = preferencias.getString("email", "vacio");
//        userPass = preferencias.getString("pass", "vacio");
//
//        if (edtUserEmail.getText().toString().equals(userEmail) && edtUserPass.getText().toString().equals(userPass)) {
//            return true; // Con true puede entrar en sesion si posteriormente se comprueba que el ususario existe en la bd
//
//        } else {
//            return false;
//        }
//    }

    // Comprueba que el email que recibe como parametro esta en la bd, llama al método getNameByEmail(), para sesion No google
    public void validateUser(String userEmail) {

        webApiRequest.validateUser(userEmail, userPass, new WebApiRequest.WebApiRequestJsonResponseListener() {
            @Override
            public void onSuccess(JsonResponseModel response) {
                Log.d("DEBUGME", "LoginActivity: User logged" + " " + response.getId() + " " + response.getMessage());
                getNameByEmail(userEmail, userPass);
            }

            @Override
            public void onError(JsonResponseModel response) {
                if (response.getId() == -210) {
                    Log.d("DEBUGME", "loginActivity, User or password failed: " + response.getId());
                    Toast.makeText(getApplicationContext(), getString(R.string.login_failure), Toast.LENGTH_LONG).show();
                } else {
                    Log.d("DEBUGME", "loginActivity: " + response.getId());
                    Toast.makeText(getApplicationContext(), getString(R.string.login_failure), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // Consulta y recibe el nombre del usuario de la bd, e inicia sesion No google, graba shared preferences
    public void getNameByEmail(String email, String password) {
        webApiRequest.getNameByEmail(email, password, new WebApiRequest.WebApiRequestJsonObjectListener_getName() {
            @Override
            public void onSuccess(int id, String message, String name) {
                if (id > 0) {
                    Log.d("DEBUGME", "Name recived: " + name);
                    //     Toast.makeText(context, "recibido nombre: " + name, Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = getSharedPreferences("savedData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", userEmail);
                    editor.putString("pass", userPass);
                    editor.putString("name", name);
                    editor.apply();
                    signIn();

                } else if (id < 0) {
                    Log.d("DEBUGME", "Email without userName");
                }
            }

            @Override
            public void onError(int id, String message) {
                Log.d("DEBUGME", "FATAL Volley error: " + id + " " + message);
            }
        });
    }

    // Utilidad de desarrollo, solo para tester
//    private void deleteAllSharedPreferences() {
//        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
//        preferencias.edit().clear().apply();
//    }


    //Método de que invoca el Intent para pantalla de iniciar sesión
    private void signIn() {
        Intent signInIntent = new Intent(getApplicationContext(), HomeActivity.class);
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signInIntent);
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión con Google
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}