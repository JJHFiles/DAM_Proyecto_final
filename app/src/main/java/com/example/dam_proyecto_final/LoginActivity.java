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

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dam_proyecto_final.home.HomeActivity;
import com.example.dam_proyecto_final.home.model.User;
import com.example.dam_proyecto_final.registry.RegistryActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    private Button btnsignGoogle, btnSignIn, btnSignUp;

    private static final int RC_SIGN_IN = 1, C_LOGIN_STR = 1;
    private GoogleSignInClient mGoogleSignInClient;



    private User user;

    private EditText edtuserEmail, edtPass;

    private String userEmail = "", userPass = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Chequea si el usuario ya existe en el dispositivo
        deleteAllSharedPreferences();
        checkSharedPreferences();

        Log.d("DEBUGME ", "metodo onCreate");

        //Instanciamos botones establecemos el listeners
        btnsignGoogle = findViewById(R.id.btnSignGoogle);
        btnsignGoogle.setOnClickListener(this);
        btnSignIn = findViewById(R.id.btnSignin);
        btnSignIn.setOnClickListener(this);
        btnSignUp = findViewById(R.id.btnSignup);
        btnSignUp.setOnClickListener(this);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);


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
        edtPass = findViewById(R.id.edtPass);
        edtPass.setOnFocusChangeListener(this);
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
                //

                //Iniciamos sesión
                Intent intent = new Intent(this, HomeActivity.class);
                //TODO crear instancia de user y pasarla como bundle
                startActivity(intent);

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
                        && !edtPass.getText().toString().contains(getResources().getString(R.string.edtpass_text))
                        && edtPass.getText().toString().length() >= getResources().getInteger(R.integer.min_pass_length)) //4
                {
                    checkSharedPreferences();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.login_failure), Toast.LENGTH_LONG).show();

                }
                break;

            case R.id.btnSignup:
                Intent registryIntent = new Intent(getApplicationContext(), RegistryActivity.class);
                startActivity(registryIntent);
                break;

            case R.id.btn:

                query("jjh@gmail.es","1234","PEPE",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
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
            case R.id.edtPass:
                edt.setText("");
                break;
        }
    }

    // Chequea si ya existe el usuario creado
    private void checkSharedPreferences() {
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        userEmail = preferencias.getString("email", "vacio");
        userPass = preferencias.getString("pass", "vacio");

        if (userEmail.equals("vacio")) {
            // TODO Lanzar el tutorial si el usuario no esta creado y es la primera vez que abre la app (Controlarlo también con Shared preferences).
            Toast.makeText(this, getResources().getString(R.string.sharedPreferences_empty), Toast.LENGTH_LONG).show();

        } else /*if(
                edtuserEmail.getText().toString().equals(email)
                && edtPass.getText().toString().equals(pass)
               )*/ {
            signIn();
        }
    }


    // Utilidad de desarrollo, solo para tester
    private void deleteAllSharedPreferences() {
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        preferencias.edit().clear().apply();
        Toast.makeText(this, "Shared Preferences eliminadas", Toast.LENGTH_LONG).show();
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión usuarios No-Google
    private void signIn() {
        Intent signInIntent = new Intent(getApplicationContext(), HomeActivity.class);
        //  createSharedPreferences();
        startActivity(signInIntent);
    }


////////////////////////////////////////////////


    private Button btn;
/*
    private final String _email = "a@a.es";
    private final String _password = "1111";
    private final String _user = "javi";
    private final String _date_signup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
*/
    private RequestQueue requestQueue;

    private static final String URL1 = "http://192.168.1.23/crud/user_insert.php";


    public void query(final String email,final String password,final String user,final String date_signup) {

        requestQueue = Volley.newRequestQueue(this);

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Response= " + response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "ERROR= " + error, Toast.LENGTH_LONG).show();
                        Log.d("DEBUGME",error.getMessage());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("email", email);
                params.put("password", password);
                params.put("name", user);
                params.put("date_signup", date_signup);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
       // Toast.makeText(getApplicationContext(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"", Toast.LENGTH_LONG).show();

        requestQueue.add(sr);
    }



}//End Class