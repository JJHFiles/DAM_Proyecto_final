package com.example.dam_proyecto_final.web_api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebApiRequest {

    private static final String URL = "http://192.168.1.23/webservice/";


    private int id;
    private String message;

    Context context;

    public WebApiRequest(Context context) {
        this.context = context;
    }

    //Método callback
    public interface WebApiRequestListener{
        void onSuccess(int result);
        void onError(int result);
    }

    //Método callback
    public interface WebApiRequestJsonObjectListener{
        void onSuccess(int id, String message);
        void onError(int id, String message);
    }

    //Peticion de inicio/registro mediante NO Google
    public void userInsert(String email, String pass, String name, WebApiRequestJsonObjectListener webapirequestjsonobjectlistener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "user_insert.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "userInsert onResponse: response " + response);

                //Respuesta
                try {
                    JSONObject json = new JSONObject(response);
                    id = json.getInt("id");
                    message = json.getString("message");
                } catch (JSONException e) {
                    webapirequestjsonobjectlistener.onError(-2, "userInsert JSONException: Error al generar el objeto JSON");
                }

                webapirequestjsonobjectlistener.onSuccess(id, message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "userInsert VolleyError: " + error.getMessage());
                webapirequestjsonobjectlistener.onError(-1, "userInsert vVolleyError: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Date date = new Date();
                String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
                Log.d("DEBUGME", "getparams: " + email + " " + name + " " + now);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", pass);
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

    //Peticion de inicio/registro mediante Google
    public void userInsertG(String email, String name, WebApiRequestJsonObjectListener webapirequestjsonobjectlistener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "user_insert_g.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "userInsertG onResponse: response " + response);

                //Respuesta
                try {
                    JSONObject json = new JSONObject(response);
                    id = json.getInt("id");
                    message = json.getString("message");
                } catch (JSONException e) {
                    webapirequestjsonobjectlistener.onError(-2, "userInsertG JSONException: Error al generar el objeto JSON");
                }

                webapirequestjsonobjectlistener.onSuccess(id, message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "userInsertG VolleyError: " + error.getMessage());
                webapirequestjsonobjectlistener.onError(-1, "userInsertG vVolleyError: " + error.getMessage());
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
