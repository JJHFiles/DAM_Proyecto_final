package com.example.dam_proyecto_final.web_api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dam_proyecto_final.Model.Group;
import com.example.dam_proyecto_final.Model.JsonResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebApiRequest {

    private static final String URL = "http://192.168.1.23/webservice/";


    private int id;
    private String message;

    Context context;

    public boolean isUserInBD = false;

    public WebApiRequest(Context context) {
        this.context = context;
    }

    //Método callback
    public interface WebApiRequestListener {
        void onSuccess(int result);

        void onError(int result);
    }

    //Método callback1
    public interface WebApiRequestJsonObjectListener {
        void onSuccess(int id, String message);
        void onError(int id, String message);
    }

    //Método callback2
    public interface WebApiRequestJsonObjectListener_getName {
        void onSuccess(int id, String message, String name);
        void onError(int id, String message);
    }

    //Método callback2
    public interface WebApiRequestJsonObjectArrayListener {
        void onSuccess(JsonResponse response, List<?> data);
        void onError(JsonResponse response);
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

    //Comprobacion, si existe el usuario creado en BD
    public void isUserInBd(String email, WebApiRequestJsonObjectListener webapirequestjsonobjectlistener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "user_getEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "userGetEmail onResponse: response " + response);

                //Respuesta
                try {
                    JSONObject json = new JSONObject(response);
                    id = json.getInt("id");
                    message = json.getString("message");

                } catch (JSONException e) {
                    webapirequestjsonobjectlistener.onError(-2, "userGetEmail JSONException: Error al generar el objeto JSON");
                }

                webapirequestjsonobjectlistener.onSuccess(id, message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "userInsert VolleyError: " + error.getMessage());
                webapirequestjsonobjectlistener.onError(-1, "userGetEmail vVolleyError: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + email);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);

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

    public void getNameByEmail(String email, WebApiRequestJsonObjectListener_getName webApiRequestJsonObjectListener_getName) {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "user_getNameByEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "getNameByEmail onResponse: response " + response);

                //Respuesta

                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject data = json.getJSONObject("name");


                    id = json.getInt("id");
                    message = json.getString("message");
                    String  name = data.getString("name");
                    webApiRequestJsonObjectListener_getName.onSuccess(id, message,name);
                    Toast.makeText(context, "Resultado nombre= " + message, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    webApiRequestJsonObjectListener_getName.onError(-2, "getNameByEmail JSONException: Error al generar el objeto JSON");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "getNameByEmail VolleyError: " + error.getMessage());
                webApiRequestJsonObjectListener_getName.onError(-1, "getNameByEmail vVolleyError: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + email);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);

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
                Log.d("DEBUGME", "getEmail onResponse: response " + response);

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

    public void getGroupsByEmail(String email, String password, WebApiRequestJsonObjectArrayListener webApiRequestJsonObjectArrayListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "getGroupsByEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "getGroupsByEmail onResponse: response " + response);

                //Respuesta
                try {
                    //Obtenemos el JsonResponde Padre
                    JSONObject json = new JSONObject(response);
                    //Obtenemos el JsonObject hijo con la respuesta
                    JSONObject jsonObjectResponse = json.getJSONObject("response");
                    JsonResponse jsonResponse = new JsonResponse(
                            jsonObjectResponse.getInt("id"),
                            jsonObjectResponse.getString("message"));

                    //Creamos la lista de grupos
                    ArrayList<Group> grupos = new ArrayList<Group>();
                    if (jsonResponse.getId() > 0){
                        //Si la respuesta es Positiva hay datos. Obtenemos el JsonArray
                        JSONArray jsonArrayGroups = json.getJSONArray("groups");
                        for (int i=0; i<jsonArrayGroups.length(); i++){
                            //Obtenemos el objeto JSONObjet de grupo individual
                            JSONObject jsonObjectGroup = jsonArrayGroups.getJSONObject(i);
                            grupos.add(new Group(
                                    jsonObjectGroup.getInt("groupid"),
                                    jsonObjectGroup.getString("groupname"),
                                    jsonObjectGroup.getString("groupdescription")));
                        }

                        //Una vez tenemos la respuesta y la lista la retornamos
                        webApiRequestJsonObjectArrayListener.onSuccess(jsonResponse, grupos);
                    } else {
                        //Si la respuesta es negativa devolvemos el error
                        webApiRequestJsonObjectArrayListener.onError(jsonResponse);
                    }

                } catch (JSONException e) {
                    webApiRequestJsonObjectArrayListener.onError(new JsonResponse(-2, "getGroupsByEmail JSONException: Error al generar el objeto JSON"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "getGroupsByEmail VolleyError: " + error.getMessage());
                webApiRequestJsonObjectArrayListener.onError(new JsonResponse(-1, "getGroupsByEmail onErrorResponse: " + error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + email + " " + password);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

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
