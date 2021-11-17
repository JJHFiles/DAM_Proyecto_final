//package com.example.dam_proyecto_final.ui.registry;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.dam_proyecto_final.R;
//import com.example.dam_proyecto_final.ui.home.HomeActivity;
//import com.example.dam_proyecto_final.web_api.WebApiRequest;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.material.textfield.TextInputEditText;
//
//public class RegistryGoogleActivity extends AppCompatActivity implements View.OnClickListener {
//
////    private TextView txtv_RGADescription;
////    private TextInputEditText edt_ARGPassword;
////    private int state;
////    private String password1;
////    private String password2;
////    private Button btn_ARGCancel;
////    private Button btn_ARGContinue;
////    private Context context;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_registry_google);
////
////        state = 0;
////
////        context = getApplicationContext();
////        txtv_RGADescription = findViewById(R.id.txtv_RGADescription);
////        edt_ARGPassword = findViewById(R.id.edt_ARGPassword);
////        btn_ARGCancel = findViewById(R.id.btn_ARGCancel);
////        btn_ARGCancel.setOnClickListener(this);
////        btn_ARGContinue = findViewById(R.id.btn_ARGContinue);
////        btn_ARGContinue.setOnClickListener(this);
////    }
////
////    @Override
////    public void onClick(View view) {
////        if (view.getId() == R.id.btn_ARGCancel){
////            finish();
////            overridePendingTransition(0, 0);
////            startActivity(getIntent());
////            overridePendingTransition(0, 0);
////        } else if (view.getId() == R.id.btn_ARGContinue){
////            switch (state){
////                case 0:
////                    //Si el campo no está vacio
////                    if (edt_ARGPassword.getText().toString().equals("")){
////                        state = 1;
////                        password1 = edt_ARGPassword.getText().toString();
////                        edt_ARGPassword.setText("");
////                        edt_ARGPassword.setSelected(false);
////                        txtv_RGADescription.setText(R.string.txtv_RGADescription_1);
////                    } else {
////                        Toast.makeText(this, "El campo contraseña está vacio", Toast.LENGTH_LONG).show();
////                    }
////                    break;
////                case 1:
////                    //Comparar contraseñas
////                    //Si el campo no está vacio
////                    if (edt_ARGPassword.getText().toString().equals("")){
////                        password2 = edt_ARGPassword.getText().toString();
////                        edt_ARGPassword.setText("");
////                        edt_ARGPassword.setSelected(false);
////
////                        //Comparo contraseñas si ok registro
////                        if (password1.equals(password2)){
////                            //Registro
////                            GoogleSignInAccount account = (GoogleSignInAccount) getIntent().getSerializableExtra("account");
////                            WebApiRequest webApiRequest = new WebApiRequest(this);
////                            webApiRequest.userInsertG(account.getEmail(), account.getDisplayName(), account.getIdToken(), new WebApiRequest.WebApiRequestJsonObjectListener() {
////                            @Override
////                            public void onSuccess(int id, String message) {
////                                if (id > 0) {
////                                    Log.d("DEBUGME", "RegistryGoogleActivity onSucess: " + id + " " + message);
////
////                                    //Guardamos el usuario en las SharedPreferences
////                                    SharedPreferences preferences = getSharedPreferences("savedData", getApplicationContext().MODE_PRIVATE);
////                                    SharedPreferences.Editor editor = preferences.edit();
////                                    editor.putString("email", account.getEmail());
////                                    editor.putString("name", account.getDisplayName());
////                                    editor.putString("pass", password1);
////                                    editor.apply();
////
////                                    //Iniciamos sesión
////                                    Intent signInIntent = new Intent(getApplicationContext(), HomeActivity.class);
////                                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                                    startActivity(signInIntent);
////                                } else if (id < 0) {
////                                    Log.d("DEBUGME", "RegistryGoogleActivity onSucess: " + id + " " + message);
////                                    Toast.makeText(context, "Error al inicar sesión. Codigo de error: " + id, Toast.LENGTH_LONG).show();
////                                }
////                            }
////
////                            @Override
////                            public void onError(int id, String message) {
////                                Log.d("DEBUGME", "loginactivity onerror: " + id + " " + message);
////                                Toast.makeText(context, "Error al inicar sesión. Codigo de error: " + id, Toast.LENGTH_LONG).show();
////                            }
////                        });
////
////                        } else {
////                            state = 0;
////                            txtv_RGADescription.setText(R.string.txtv_RGADescription_0);
////                            edt_ARGPassword.setText("");
////                            edt_ARGPassword.setSelected(false);
////                            Toast.makeText(this, "Las contraseñas no coinciden, por favor introducelas de nuevo", Toast.LENGTH_LONG).show();
////                        }
////
////                    } else {
////                        Toast.makeText(this, "El campo contraseña está vacio", Toast.LENGTH_LONG).show();
////                    }
////                    break;
////
////            }
////        }
////    }
//}