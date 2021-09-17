package com.example.dam_proyecto_final.web_api;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class MySqlQuery {

    private Context context;

    private String email = "";
    private String password = "";
    private String user = "";
    private String date_singup = "";

    private RequestQueue requestQueue;

    private static final String url1 = "http://192.168.1.23/crud/user_insert.php";

    public MySqlQuery(Context cont) {
        context = cont;


    }

    public void init() {

      requestQueue = newRequestQueue(context);
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                url1,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        Toast.makeText(context, "Response= " + response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR= " + error, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("name", user);
                params.put("date_singup", date_singup);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        Toast.makeText(context, "FIN", Toast.LENGTH_LONG).show();

        requestQueue.add(sr);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate_singup() {
        return date_singup;
    }

    // Guarda la fecha y hora actual
    public void setDate_singup() {

        date_singup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    }


}
