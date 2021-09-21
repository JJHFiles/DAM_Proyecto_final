package com.example.dam_proyecto_final.registry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.home.HomeActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;

public class RegistryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtvQuestion;
    private EditText edtInput;
    private Button btnCancel, btnContinue;
    private int step = 0; //0->name, 1->email, 2->pass first, 3->pass second

    private String userName = "No introducido", userEmail = "No introducido", userPass = "";

    private WebApiRequest webapirequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        userPass = getResources().getString(R.string.edtpass_text);

        //Muestra flecha de retroceso del ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtvQuestion = findViewById(R.id.txtvQuestion);
        edtInput = findViewById(R.id.edtInput);
        btnCancel = findViewById(R.id.btnCancel);
        btnContinue = findViewById(R.id.btnContinue);
        btnCancel.setOnClickListener(this);
        btnContinue.setOnClickListener(this);

        //WebApiRequest
        webapirequest = new WebApiRequest(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnCancel:
                // Cierra la actividad y vuelve  la ventana login
                finish();
                break;

            case R.id.btnContinue:

                switch (step) {

                    case 0:
                        // Se comprueba que se introduzca  como minimo 1 caracter
                        if (edtInput.getText().toString().length() >= getResources().getInteger(R.integer.min_name_length)) //1
                        {
                            userName = edtInput.getText().toString();
                            txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_email));
                            edtInput.setText("");
                            edtInput.setHint(getResources().getString(R.string.edtInput_email));

                            step = 1;

                        } else {
                            Toast.makeText(this, R.string.name_empty, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 1:
                        if (Patterns.EMAIL_ADDRESS.matcher(edtInput.getText().toString()).matches()) {
                            userEmail = edtInput.getText().toString();
                            txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_passFirst));
                            edtInput.setText("");
                            edtInput.setHint(getResources().getString(R.string.edtInput_pass));

                            step = 2;
                        } else {
                            Toast.makeText(this, R.string.email_NoMatch, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 2:
                        if (!edtInput.getText().toString().contains(getResources().getString(R.string.edtpass_text))
                                && edtInput.getText().toString().length() >= getResources().getInteger(R.integer.min_pass_length)) //4)
                        {
                            step = 3;
                            userPass = edtInput.getText().toString();
                            edtInput.setText(R.string.btnContinue_Finalize);
                            txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_passSecond));
                            edtInput.setText("");
                            edtInput.setHint(getResources().getString(R.string.edtInput_pass));


                        } else {
                            Toast.makeText(this, R.string.password_failure, Toast.LENGTH_LONG).show();

                        }
                        break;

                    case 3:
                        if (edtInput.getText().toString().equals(userPass)) {

                            // Graba el usuario en las Shared preferences
                          //  createSharedPreferences();

                            // Si el usuario no exise en la bd lo crea nuevo y graba shared preferences

                                isertUserInBD();


                            signIn();
                        } else {
                            Toast.makeText(this, R.string.pass_NotEquals, Toast.LENGTH_LONG).show();
                        }
                        break; //fin de -> case 3:
                }
                break; //fin de -> case R.id.btnContinue:
        }
    }
/*
    private void createSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("savedData", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("email", userEmail);
        editor.putString("pass", userPass);
        editor.putString("name", userName);
        editor.apply();
    }

 */

    // NO SE USA - Chequea si ya existe el usuario creado y hace singIn
    private void checkSharedPreferences() {
        SharedPreferences preferencias = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        String email = preferencias.getString("email", "vacio");
        String pass = preferencias.getString("pass", "vacio");

        if (userEmail.equals("vacio")) {
            // TODO Lanzar el tutorial si el usuario no esta creado y es la primera vez que abre la app (Controlarlo también con Shared preferences).
            // TODO Controlar si el usuario esta logeado con Google

            Toast.makeText(this, getResources().getString(R.string.sharedPreferences_empty), Toast.LENGTH_LONG).show();

        } else if (userEmail.equals(email) && userPass.equals(pass)) {
            signIn();
        }
    }



    public void isertUserInBD() {
        webapirequest.userInsert(userEmail, userPass, userName, new WebApiRequest.WebApiRequestJsonObjectListener() {
            @Override
            public void onSuccess(int id, String message) {
                if (id > 0) {
                    Log.d("DEBUGME", "loginactivity onSucess: " + id + " " + message);

                    //Guardamos el usuario en las SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("savedData", getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", userEmail);
                    editor.putString("pass", userPass);
                    editor.putString("name", userName);
                    editor.apply();

                    //Iniciamos sesión
                    signIn();
                } else if (id < 0) {
                    Log.d("DEBUGME", "loginactivity onSucess: " + id + " " + message);
                    Toast.makeText(getApplicationContext(), "Error al inicar sesión. Codigo de error: " + id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(int id, String message) {
                Log.d("DEBUGME", "loginactivity onerror: " + id + " " + message);
                Toast.makeText(getApplicationContext(), "Error al inicar sesión. Codigo de error: " + id, Toast.LENGTH_LONG).show();
            }
        });
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión usuarios No-Google
    private void signIn() {
        Intent signInIntent = new Intent(getApplicationContext(), HomeActivity.class);
        //  createSharedPreferences();
        startActivity(signInIntent);
    }

    // Método que controla el click en la flecha ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Retrocede a la opc anterior dentro de la misma actividad
    @Override
    public void onBackPressed() {
        step--;
        switch (step) {
            case -1:
                // Cierra la actividad y vuelve  la ventana login
                finish();
                break;
            case 0:
                btnContinue.setText(R.string.btnContinue_Continue);
                txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_name));
                edtInput.setText("");
                edtInput.setHint(getResources().getString(R.string.edtInput_name));
                break;

            case 1:
                btnContinue.setText(R.string.btnContinue_Continue);
                txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_email));
                edtInput.setText("");
                edtInput.setHint(getResources().getString(R.string.edtInput_email));
                break;

            case 2:
                btnContinue.setText(R.string.btnContinue_Continue);
                txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_passFirst));
                edtInput.setText("");
                edtInput.setHint(getResources().getString(R.string.edtInput_pass));
                break;
        }
    }
}