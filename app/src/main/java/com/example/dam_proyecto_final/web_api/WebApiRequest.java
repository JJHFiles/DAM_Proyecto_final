package com.example.dam_proyecto_final.web_api;

import android.content.Context;
import android.util.Log;

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

    private static final String URL = "http://192.168.1.37/webservice/";
    private int result;
    Context context;

    public WebApiRequest(Context context) {
        this.result = -101;
        this.context = context;
    }

    //Método callback
    public interface WebApiRequestListener{
        void onSuccess(int result);
        void onError(int result);
    }

    //Peticion de inicio/registro mediante NO Google
    public void userInsert(String email, String password, String name, WebApiRequestListener webapirequestlistener){

    }

    //Peticion de inicio/registro mediante Google
    public void userInsertG(String email, String name, WebApiRequestListener webapirequestlistener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "user_insert_g.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "onResponse: response " + response);
                //Respuesta
                result = Integer.parseInt(response);
                webapirequestlistener.onSuccess(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "Volley error: " + error.getMessage());
                webapirequestlistener.onError(-100);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Date date = new Date();
                String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
                Log.d("DEBUGME", "getparams: " + email + " " + name + " " + now);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("name", name);
                params.put("date_signup", now);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);

    }
}
