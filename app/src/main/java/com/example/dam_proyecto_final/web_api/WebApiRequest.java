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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebApiRequest {

    static int result = -100;

    //Peticion de inicio/registro mediante Google
    public static Integer userInsertG(Context context, String email, String name){

        result = -100;

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://192.168.1.37/crud/user_insert_g.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "onResponse: response " + response);
                //Respuesta
                result = Integer.parseInt(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "Volley error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Date date = new Date();
                String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
                Log.d("DEBUGME", "getparams: " + email + " " + name + " " + now);
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("name", name);
                params.put("date_signup", now);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);


        if (result == -100){
            Log.d("DEBUGME ", "userInsertG: " + " error al obtener resultados");
            return result;
        } else {
            return result;
        }
    }
}
