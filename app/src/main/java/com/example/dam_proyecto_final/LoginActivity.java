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
import com.example.dam_proyecto_final.registry.RegistryActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    private Button btnsignGoogle, btnSignIn, btnSignUp;

    private static final int RC_SIGN_IN = 1, C_LOGIN_STR = 1;
    private GoogleSignInClient mGoogleSignInClient;

    private WebApiRequest webapirequest;
    private Context context;


    private EditText edtuserEmail, edtUserPass;

    private String userEmail = "", userPass = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //WebApiRequest
        webapirequest = new WebApiRequest(this);
        context = getApplicationContext();

        // Chequea si el usuario ya existe en el dispositivo
        //deleteAllSharedPreferences(); //Dejamos de borrar para probar el guardado de login
        //checkSharedPreferences(); // Esta comprobación la realiza onStart();

        Log.d("DEBUGME ", "metodo onCreate");

        //Instanciamos botones establecemos el listeners
        btnsignGoogle = findViewById(R.id.btnSignGoogle);
        btnsignGoogle.setOnClickListener(this);
        btnSignIn = findViewById(R.id.btnSignin);
        btnSignIn.setOnClickListener(this);
        btnSignUp = findViewById(R.id.btnSignup);
        btnSignUp.setOnClickListener(this);


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
        edtUserPass = findViewById(R.id.edtUserPass);
        edtUserPass.setOnFocusChangeListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        Log.d("DEBUGME ", "metodo on start");

        //TODO se ve ligeramente la ventana antes de ir a la home, seguramente esta comprobación hab´ra que hacerla en una activity previa

        //Comprobamos si existe previamente un usuario Google logeado
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //Comprobamos previamente si existe un usuario por SharedPreferences Logeado
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", null);

        //Si alguno de los dos no es nulo hay inicio de sesión previo
        if (account != null || userEmail != null) {
            Log.d("DEBUGME ", "LoginActivity onStart: inicio de sesión cacheado");
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

                //TODO Comprobar si el usuario que inicia lo tenemos registrado en BBDD, si no es así registrarlo como nuevo usuario sin password
                webapirequest.userInsertG(account.getEmail(), account.getDisplayName(), new WebApiRequest.WebApiRequestJsonObjectListener() {
                    @Override
                    public void onSuccess(int id, String message) {
                        if (id > 0) {
                            Log.d("DEBUGME", "loginactivity onSucess: " + id + " " + message);

                            //Guardamos el usuario en las SharedPreferences
                            SharedPreferences preferences = getSharedPreferences("savedData", getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", account.getEmail());
                            editor.putString("name", account.getDisplayName());
                            editor.apply();

                            //Iniciamos sesión
                            signIn();
                        } else if (id < 0) {
                            Log.d("DEBUGME", "loginactivity onSucess: " + id + " " + message);
                            Toast.makeText(context, "Error al inicar sesión. Codigo de error: " + id, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(int id, String message) {
                        Log.d("DEBUGME", "loginactivity onerror: " + id + " " + message);
                        Toast.makeText(context, "Error al inicar sesión. Codigo de error: " + id, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("DEBUGME ", "Google sign in failed", e);
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                //txtvdebug.setText(e.getMessage());
            }
        } else {
            Toast.makeText(context, "requestCode != RC_SIGN_IN", Toast.LENGTH_LONG).show();

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

                // Si se cumplen formato y tamaño de contraseña
                if (Patterns.EMAIL_ADDRESS.matcher(edtuserEmail.getText().toString()).matches()
                        && !edtUserPass.getText().toString().contains(getResources().getString(R.string.edtpass_text))
                        && edtUserPass.getText().toString().length() >= getResources().getInteger(R.integer.min_pass_length)) //4
                {
                    // Si el usuario esta en sharedPreferences y en bd
                    if (checkSharedPreferences() && isUserEmailInBD(edtUserPass.getText().toString())) {
                        signIn();
                    } else {
                        Toast.makeText(this, "El usuario no existe en la BD, registrese primero, gracias", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.login_failure), Toast.LENGTH_LONG).show();

                }
                break;

            case R.id.btnSignup:
                Intent registryIntent = new Intent(getApplicationContext(), RegistryActivity.class);
                startActivity(registryIntent);
                break;

        }
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión con Google
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Metodo de limpieza de los EditText tras hacerlos foco
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
            case R.id.edtUserPass:
                edt.setText("");
                break;
        }
    }

    // Chequea si ya existe el usuario creado
    private boolean checkSharedPreferences() {
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", "vacio");
        userPass = preferencias.getString("pass", "vacio");

        if (userEmail.equals("vacio")) {
            Toast.makeText(this, getResources().getString(R.string.sharedPreferences_empty), Toast.LENGTH_LONG).show();
            return false;

        } else if (edtuserEmail.getText().toString().equals(userEmail) && edtUserPass.getText().toString().equals(userPass)) {
            return true; // con true puede entrar en sesion si posteriormente se comprueba que el ususario existe en la bd

        } else {
            return false;
        }
    }

    public boolean isUserEmailInBD(String userEmail) {

        // TODO get userEmail from bd, comprobar si existe userEmail en bd
        return true;
    }


    // Utilidad de desarrollo, solo para tester
    private void deleteAllSharedPreferences() {
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        preferencias.edit().clear().apply();
        Toast.makeText(this, "Shared Preferences eliminadas", Toast.LENGTH_LONG).show();
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión
    private void signIn() {
        Intent signInIntent = new Intent(getApplicationContext(), HomeActivity.class);
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //  createSharedPreferences();
        startActivity(signInIntent);
    }


}//End Class