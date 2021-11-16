package com.example.dam_proyecto_final.ui.home.homeui.profileui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.ui.LoginActivity;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.textfield.TextInputEditText;

public class PasswordChangeActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private int state = 0;

    private String userEmail;
    private String userPass;


    private String newPass;


    private TextView tvChangePass;
    private TextInputEditText tietPass;
    private Button btnNextPassChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        this.setTitle(getString(R.string.change_password));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvChangePass = findViewById(R.id.tv_profile_changepass);
        tietPass = findViewById(R.id.tiet_profile_pass);
        tietPass.addTextChangedListener(this);


        btnNextPassChange = findViewById(R.id.btn_profile_nextpasschange);
        btnNextPassChange.setOnClickListener(this);
        Button btnCancelPassChange = findViewById(R.id.btn_profile_cancelpasschange);
        btnCancelPassChange.setOnClickListener(this);

        userEmail = getIntent().getExtras().getString("userEmail", "vacio");
        userPass = getIntent().getExtras().getString("userPass", "vacio");

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_profile_cancelpasschange){
            finish();
        } else if (view.getId() == R.id.btn_profile_nextpasschange){
            if (state == 0){
                if (tietPass.getText().toString().equals(userPass)){
                    state++;
                    tvChangePass.setText(R.string.password_change_text2);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.password_change_warning_baddpassword)
                            , Toast.LENGTH_LONG).show();
                }
                tietPass.setText("");
                btnNextPassChange.setEnabled(false);
            } else if (state == 1){
                state++;
                newPass = tietPass.getText().toString();
                tvChangePass.setText(R.string.password_change_text3);
                tietPass.setText("");
                btnNextPassChange.setEnabled(false);
            }
            else if (state == 2){
                String newPass2 = tietPass.getText().toString();
                if (newPass.equals(newPass2)){
                    new WebApiRequest(getApplicationContext()).updatePassword(userEmail, userPass, newPass, new WebApiRequest.WebApiRequestJsonResponseListener() {
                        @Override
                        public void onSuccess(JsonResponseModel response) {

                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("savedData", Context.MODE_PRIVATE);
                            preferences.edit().remove("email").apply();
                            preferences.edit().remove("pass").apply();

                            //Volvemos al login activity
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), getString(R.string.password_change_passwordchanged)
                                    , Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onError(JsonResponseModel response) {
                            Toast.makeText(getApplicationContext(), getString(R.string.password_change_warning_errorchanging) + response.getId()
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    state = 1;
                    tvChangePass.setText(R.string.password_change_text2);
                    tietPass.setText("");
                    btnNextPassChange.setEnabled(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.password_change_warning_newpassnotequals)
                            , Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        btnNextPassChange.setEnabled(tietPass.getText().toString().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}