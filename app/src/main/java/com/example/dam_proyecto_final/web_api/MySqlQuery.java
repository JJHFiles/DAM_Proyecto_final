package com.example.dam_proyecto_final.web_api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MySqlQuery {

    private Context context;

    private String email = "";
    private String password = "";
    private String user = "";
    private String date_signup = "";

    private RequestQueue requestQueue;

    private static final String URL1 = "http://192.168.1.23/crud/user_insert.php";

    private int result = -100;



    public MySqlQuery(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);

    }

    public void insertUser(String email, String password, String user, String date_signup) {

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "onResponse= " + response, Toast.LENGTH_LONG).show();
                        Log.d("DEBUGME", "onResponse: " + response);
                        result = Integer.parseInt(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "onErrorResponse= " + error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("DEBUGME", "onErrorResponse: " + error.getMessage());
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

        requestQueue.add(sr);
/*
        if (result == -100){
            Log.d("DEBUGME ", "userInsertG: " + " error al obtener resultados");
            return result;
        } else {
            return result;
        }
*/

    }


    public int getResult() {
        return result;
    }

}
