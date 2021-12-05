package com.example.dam_proyecto_final.ui.registry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.ui.LoginActivity;
import com.example.dam_proyecto_final.ui.home.HomeActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// Para el registro de usuarios NO Google
public class RegistryActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextView txtvQuestion;
    private TextInputEditText txInEdTx;
    private TextInputLayout txInLaHint;
    private Button btnCancel, btnContinue;
    private int step = 0; //0->name, 1->email, 2->pass first, 3->pass second
//    String charactersLimits;

    private String userName = "", userEmail = "", userPass = "";

    private WebApiRequest webApiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        userPass = getResources().getString(R.string.edtpass_text);

        //Muestra flecha de retroceso del ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtvQuestion = findViewById(R.id.txtvQuestion);
        txInEdTx = findViewById(R.id.txInEdTx);
        txInEdTx.addTextChangedListener(this);
        txInLaHint = findViewById(R.id.txInLaHint);
        btnCancel = findViewById(R.id.btnCancel);
        btnContinue = findViewById(R.id.btnContinue);
        btnCancel.setOnClickListener(this);
        btnContinue.setOnClickListener(this);

        //WebApiRequest
        webApiRequest = new WebApiRequest(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnCancel:
                // Cierra la actividad y vuelve  la ventana login
                goToLogin();
                break;

            case R.id.btnContinue:

                switch (step) {

                    case 0:// introduccion del nombre, primer paso
                        // Se comprueba que se introduzca  como minimo 1 caracter
                        if (txInEdTx.getText().toString().length() >= getResources().getInteger(R.integer.min_name_length)) //1
                        {
                            userName = txInEdTx.getText().toString();
                            txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_email));
                            txInEdTx.setText("");
                            txInLaHint.setHint(getResources().getString(R.string.email));

                            step = 1; // se avanza al paso siguiente
                        } else {
                            Toast.makeText(this, R.string.name_empty, Toast.LENGTH_LONG).show();
                            Log.d("DEBUGME",getString(R.string.name_empty));

                        }
                        break;

                    case 1:// introduccion del email, segundo paso
                        if (Patterns.EMAIL_ADDRESS.matcher(txInEdTx.getText().toString()).matches()) {
//                            txInEdTx.setFilters(new InputFilter[] { });
                            userEmail = txInEdTx.getText().toString();
                            txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_passFirst));
                            txInEdTx.setText("");
                            txInLaHint.setHint(getResources().getString(R.string.edtInput_pass));
                            txInEdTx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            step = 2;
                        } else {
                            Toast.makeText(this, R.string.email_NoMatch, Toast.LENGTH_LONG).show();
                            Log.d("DEBUGME",getString(R.string.email_NoMatch));

                        }
                        break;

                    case 2: // introduccion de la contraseña, tercer paso
                        if (!txInEdTx.getText().toString().contains(getResources().getString(R.string.edtpass_text))
                                && txInEdTx.getText().toString().length() >= getResources().getInteger(R.integer.min_pass_length)) //4)
                        {
                            btnContinue.setText(getString(R.string.finalize));
                            step = 3;
                            userPass = txInEdTx.getText().toString();
                            txInEdTx.setText(R.string.btnContinue_Finalize);
                            txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_passSecond));
                            txInEdTx.setText("");
                            txInEdTx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            txInLaHint.setHint(getResources().getString(R.string.edtInput_pass));
                        } else {
                            Toast.makeText(this, R.string.password_failure, Toast.LENGTH_LONG).show();
                            Log.d("DEBUGME",getString(R.string.password_failure));

                        }
                        break;

                    case 3: // introduccion de la contraseña por segunda vez, verificacion de coincidencia de caracteres, cuarto y ultimo paso.
                        if (txInEdTx.getText().toString().equals(userPass)) {
                            //   isertUserInBD();
                            isUserEmailInBD(userEmail);

                        } else {
                            Toast.makeText(this, R.string.pass_NotEquals, Toast.LENGTH_LONG).show();
                            Log.d("DEBUGME",getString(R.string.pass_NotEquals));
                            txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_passFirst));
                            txInEdTx.setText("");
                            txInLaHint.setHint(getResources().getString(R.string.edtInput_pass));
                            txInEdTx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            step = 2;

                        }
                        break; //fin de -> case 3:
                }
                break; //fin de -> case R.id.btnContinue:
        }
    }

    // Comprueba si existe el usuario en la bd
    public void isUserEmailInBD(String userEmail) {
        webApiRequest.isUserEmailInBd(userEmail, new WebApiRequest.WebApiRequestJsonObjectListener() {
            @Override
            public void onSuccess(int id, String message) {
                if (id == 222) {
                    Log.d("DEBUGME", "user " + userEmail + " exist, mensa: " + message);
                    Toast.makeText(getApplicationContext(), getString(R.string.warning_registry_email_exists), Toast.LENGTH_LONG).show();

                    txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_email));
                    txInEdTx.setText("");
                    txInLaHint.setHint(getResources().getString(R.string.email));
                    txInEdTx.setInputType(InputType.TYPE_CLASS_TEXT);
                    step = 1;

                } else if (id == 223) {
                    Log.d("DEBUGME", "User not exist, received: " + id);
                    insertUserInBD();
                }
            }

            @Override
            public void onError(int id, String message) {
                Log.d("DEBUGME", "LoginActivity onError: " + id + " " + message);
             //   Toast.makeText(getApplicationContext(), getString(R.string.warning_generic_error) + id, Toast.LENGTH_LONG).show();
            }
        });
    }

    // introduce el usuario en la bd e inicia sesion No google.
    public void insertUserInBD() {
        webApiRequest.userInsert(userEmail, userPass, userName, new WebApiRequest.WebApiRequestJsonObjectListener() {
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
                    Toast.makeText(getApplicationContext(), getString(R.string.warning_generic_error), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(int id, String message) {
                Log.d("DEBUGME", "loginactivity onerror: " + id + " " + message);
             //   Toast.makeText(getApplicationContext(), getString(R.string.warning_generic_error) + id, Toast.LENGTH_LONG).show();
            }
        });
    }

    //Método de que invoca el Intent para pantalla de iniciar sesión usuarios No-Google
    private void signIn() {
        Intent signInIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(signInIntent);
    }


    // Retrocede a la opc anterior dentro de la misma actividad
    @Override
    public void onBackPressed() {
        step--;
        switch (step) {
            case -1:
                // Cierra la actividad y vuelve  la ventana login
                goToLogin();
                break;
            case 0:
                btnContinue.setText(R.string.btnContinue_Continue);
                txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_name));
                txInEdTx.setText("");
                txInEdTx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                txInLaHint.setHint(getResources().getString(R.string.edtInput_name));

                break;

            case 1:
                btnContinue.setText(R.string.btnContinue_Continue);
                txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_email));
                txInEdTx.setText("");
                txInEdTx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                txInLaHint.setHint(getResources().getString(R.string.email));
                break;

            case 2:
                btnContinue.setText(getString(R.string.btnContinue_Continue));
                btnContinue.setText(R.string.btnContinue_Continue);
                txtvQuestion.setText(getResources().getString(R.string.txtvQuestion_passFirst));
                txInEdTx.setText("");
                txInEdTx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                txInLaHint.setHint(getResources().getString(R.string.edtInput_pass));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Métodos para habilitar el botón con el form completo
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (txInEdTx.getText().toString().length() == 0) {
            btnContinue.setEnabled(false);
        } else {
            btnContinue.setEnabled(true);
        }
    }
}