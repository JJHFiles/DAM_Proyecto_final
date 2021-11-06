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
import com.example.dam_proyecto_final.model.ActivityModel;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.model.MemberModel;
import com.example.dam_proyecto_final.model.SharedModel;

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

    //private static final String URL = "http://192.168.1.23/webservice/";
    private static final String URL = "https://blist.azurewebsites.net/";


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

    //Método callback1 Devuelve un String
    public interface WebApiRequestJsonObjectListener {
        void onSuccess(int id, String message);

        void onError(int id, String message);
    }

    //Método callback2 Devuelve id, mensaje y nombre
    public interface WebApiRequestJsonObjectListener_getName {
        void onSuccess(int id, String message, String name);

        void onError(int id, String message);
    }

    //Método callback3 Devuelve una respuesta
    public interface WebApiRequestJsonResponseListener {
        void onSuccess(JsonResponseModel response);

        void onError(JsonResponseModel response);
    }

    //Método callback4 Devuelve una respuesta y una lista
    public interface WebApiRequestJsonObjectArrayListener {
        void onSuccess(JsonResponseModel response, List<?> data);

        void onError(JsonResponseModel response);
    }



    //Método callback5 Devuelve una respuesta y 2 listas
    public interface WebApiRequestJsonObjectArrayListenerV2 {
        void onSuccess(JsonResponseModel response, GroupModel group, List<?> data);

        void onError(JsonResponseModel response);
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
    public void isUserEmailInBd(String email, WebApiRequestJsonObjectListener webapirequestjsonobjectlistener) {
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

    //Comprobacion, si existe el usuario creado en BD
    public void validateUser(String email, String password, WebApiRequestJsonResponseListener webapirequestjsonresponselistener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "validateUser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "validateUser onResponse: response " + response);

                //Respuesta
                try {
                    JSONObject json = new JSONObject(response);
                    id = json.getInt("id");
                    message = json.getString("message");
                    JsonResponseModel jsonResponseModel = new JsonResponseModel(id, message);
                    if (jsonResponseModel.getId() > 0) {
                        webapirequestjsonresponselistener.onSuccess(jsonResponseModel);
                    } else {
                        webapirequestjsonresponselistener.onError(jsonResponseModel);
                    }
                } catch (JSONException e) {
                    webapirequestjsonresponselistener.onError(new JsonResponseModel(-2, "validateUser JSONException: Error al generar el objeto JSON"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "validateUser VolleyError: " + error.getMessage());
                webapirequestjsonresponselistener.onError(new JsonResponseModel(-1, "validateUser vVolleyError: " + error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + email);
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

    public void getNameByEmail(String email, String password, WebApiRequestJsonObjectListener_getName webApiRequestJsonObjectListener_getName) {

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
                    String name = data.getString("name");
                    webApiRequestJsonObjectListener_getName.onSuccess(id, message, name);


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

    //Peticion de inicio/registro mediante Google
    public void userInsertG(String email, String name, String idToken, WebApiRequestJsonObjectListener webapirequestjsonobjectlistener) {
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
                Log.d("DEBUGME", "userInsertG getParams: " + email + " " + name + " " + now);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("name", name);
                params.put("token", idToken);
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
                    JsonResponseModel jsonResponseModel = new JsonResponseModel(
                            jsonObjectResponse.getInt("id"),
                            jsonObjectResponse.getString("message"));

                    //Creamos la lista de grupos
                    ArrayList<GroupModel> grupos = new ArrayList<GroupModel>();
                    if (jsonResponseModel.getId() > 0) {
                        //Si la respuesta es Positiva hay datos. Obtenemos el JsonArray
                        JSONArray jsonArrayGroups = json.getJSONArray("groups");
                        for (int i = 0; i < jsonArrayGroups.length(); i++) {
                            //Obtenemos el objeto JSONObjet de grupo individual
                            JSONObject jsonObjectGroup = jsonArrayGroups.getJSONObject(i);
                            grupos.add(new GroupModel(
                                    jsonObjectGroup.getInt("groupid"),
                                    jsonObjectGroup.getString("groupname"),
                                    jsonObjectGroup.getString("groupdescription"),
                                    jsonObjectGroup.getString("currency")
                            ));
                        }

                        //Una vez tenemos la respuesta y la lista la retornamos
                        webApiRequestJsonObjectArrayListener.onSuccess(jsonResponseModel, grupos);
                    } else {
                        //Si la respuesta es negativa devolvemos el error
                        webApiRequestJsonObjectArrayListener.onError(jsonResponseModel);
                    }

                } catch (JSONException e) {
                    webApiRequestJsonObjectArrayListener.onError(new JsonResponseModel(-2, "getGroupsByEmail JSONException: Error al generar el objeto JSON"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "getGroupsByEmail VolleyError: " + error.getMessage());
                webApiRequestJsonObjectArrayListener.onError(new JsonResponseModel(-1, "getGroupsByEmail onErrorResponse: " + error.getMessage()));
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


    public void getActivityByEmail(String email, String password, WebApiRequestJsonObjectArrayListener webApiRequestJsonObjectArrayListener) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "getActivityByEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "getActivityByEmail onResponse: response " + response);

                //Respuesta
                try {
                    //Obtenemos el JsonResponde Padre
                    JSONObject json = new JSONObject(response);
                    //Obtenemos el JsonObject hijo con la respuesta
                    JSONObject jsonObjectResponse = json.getJSONObject("response");
                    JsonResponseModel jsonResponseModel = new JsonResponseModel(
                            jsonObjectResponse.getInt("id"),
                            jsonObjectResponse.getString("message"));

                    //Creamos la lista de grupos
                    ArrayList<ActivityModel> activityModels = new ArrayList<>();
                    if (jsonResponseModel.getId() > 0) {
                        //Si la respuesta es Positiva hay datos. Obtenemos el JsonArray
                        JSONArray jsonArrayActivity = json.getJSONArray("activity");
                        for (int i = 0; i < jsonArrayActivity.length(); i++) {
                            //Obtenemos el objeto JSONObjet de Activity individual
                            JSONObject jsonObjectActivity = jsonArrayActivity.getJSONObject(i);

                            // Comprobamos si la factura es nula
                            int identifierinvoice = -1;
                            if (!jsonObjectActivity.isNull("identifierincoice")){
                                identifierinvoice = jsonObjectActivity.getInt("identifierinvoice");
                            }
                            activityModels.add(new ActivityModel(
                                    jsonObjectActivity.getString("idactivity"),
                                    jsonObjectActivity.getString("date_activity"),
                                    jsonObjectActivity.getString("action"),
                                    jsonObjectActivity.getInt("idgroup"),
                                    jsonObjectActivity.getString("email"),
                                    identifierinvoice,
                                    jsonObjectActivity.getInt("icon")
                            ));
                        }

                        //Una vez tenemos la respuesta y la lista la retornamos
                        webApiRequestJsonObjectArrayListener.onSuccess(jsonResponseModel, activityModels);
                    } else {
                        //Si la respuesta es negativa devolvemos el error
                        webApiRequestJsonObjectArrayListener.onError(jsonResponseModel);
                    }

                } catch (JSONException e) {
                    webApiRequestJsonObjectArrayListener.onError(new JsonResponseModel(-2, "getGroupsByEmail JSONException: Error al generar el objeto JSON"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "getActivityByEmail VolleyError: " + error.getMessage());
                webApiRequestJsonObjectArrayListener.onError(new JsonResponseModel(-1, "getGroupsByEmail onErrorResponse: " + error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + email);
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

    public void getIfEmailExist(String email, WebApiRequestJsonResponseListener webApiRequestJsonResponseListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "getIfEmailExist.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "getIfEmailExist onResponse: response " + response);

                //Respuesta
                try {
                    //Obtenemos el JsonResponde Padre
                    JSONObject json = new JSONObject(response);

                    //Creamos el JsonResponseModel
                    JsonResponseModel jsonResponseModel = new JsonResponseModel(
                            json.getInt("id"),
                            json.getString("message"));

                    if (jsonResponseModel.getId() > 0) {
                        webApiRequestJsonResponseListener.onSuccess(jsonResponseModel);
                    } else {
                        webApiRequestJsonResponseListener.onError(jsonResponseModel);
                    }
                } catch (JSONException e) {
                    webApiRequestJsonResponseListener.onError(new JsonResponseModel(-2, "getIfEmailExist JSONException: Error al generar el objeto JSON"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "getGroupsByEmail VolleyError: " + error.getMessage());
                webApiRequestJsonResponseListener.onError(new JsonResponseModel(-1, "getIfEmailExist onErrorResponse: " + error.getMessage()));
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

    public void addGroup(String email, String password, String groupname, String groupdescription, String groupcurrency, ArrayList<MemberModel> memberModels, WebApiRequestJsonResponseListener webApiRequestJsonResponseListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "addGroup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "addGroup onResponse: response " + response);

                //Respuesta
                try {
                    //Obtenemos el JsonResponde Padre
                    JSONObject json = new JSONObject(response);

                    //Creamos el JsonResponseModel
                    JsonResponseModel jsonResponseModel = new JsonResponseModel(
                            json.getInt("id"),
                            json.getString("message"));

                    if (jsonResponseModel.getId() > 0) {
                        webApiRequestJsonResponseListener.onSuccess(jsonResponseModel);
                    } else {
                        webApiRequestJsonResponseListener.onError(jsonResponseModel);
                    }
                } catch (JSONException e) {
                    webApiRequestJsonResponseListener.onError(new JsonResponseModel(-2, "addGroup JSONException: Error al generar el objeto JSON"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "getGroupsByEmail VolleyError: " + error.getMessage());
                webApiRequestJsonResponseListener.onError(new JsonResponseModel(-1, "addGroup onErrorResponse: " + error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + email);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("groupname", groupname);
                params.put("groupdescription", groupdescription);
                params.put("groupcurrency", groupcurrency);

                int c = 1;
                for (MemberModel m : memberModels) {
                    String member = "member" + c;
                    String role = "role" + c;
                    Log.d("DEBUGME", m.getEmail() + " " + m.getRole() + " " + member + " " + role);
                    params.put(member, m.getEmail());
                    params.put(role, String.valueOf(m.getRole()));
                    c++;
                }

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

    public void getInvoiceByGroup(int idGroup, WebApiRequestJsonObjectArrayListener webApiRequestJsonObjectArrayListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "getInvoiceByGroup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "getInvoiceByGroup onResponse: response " + response);

                //Respuesta
                try {
                    //Obtenemos el JsonResponde Padre
                    JSONObject json = new JSONObject(response);
                    //Obtenemos el JsonObject hijo con la respuesta
                    JSONObject jsonObjectResponse = json.getJSONObject("response");
                    JsonResponseModel jsonResponseModel = new JsonResponseModel(
                            jsonObjectResponse.getInt("id"),
                            jsonObjectResponse.getString("message"));

                    //Creamos la lista de grupos
                    ArrayList<InvoiceModel> invoiceModel;
                    invoiceModel = new ArrayList<>();
                    if (jsonResponseModel.getId() > 0) {
                        //Si la respuesta es Positiva hay datos. Obtenemos el JsonArray
                        JSONArray jsonArrayInvoice = json.getJSONArray("invoice");
                        for (int i = 0; i < jsonArrayInvoice.length(); i++) {
                            //Obtenemos el objeto JSONObjet de Activity individual
                            JSONObject jsonObjectInvoice = jsonArrayInvoice.getJSONObject(i);

//                            double consumption = -1;
//                            if (!jsonObjectInvoice.getString("consumption").equals("null")){
//                                consumption = jsonObjectInvoice.getDouble("consumption");
//                            }

                            invoiceModel.add(new InvoiceModel(
                                    jsonObjectInvoice.getString("identifier"),
                                    jsonObjectInvoice.getString("type"),
                                    jsonObjectInvoice.getString("date"),
                                    jsonObjectInvoice.getString("start_period"),
                                    jsonObjectInvoice.getString("end_period"),
                                    jsonObjectInvoice.getDouble("consumption"),
                                    jsonObjectInvoice.getDouble("amount"),
                                    jsonObjectInvoice.getString("filetype"),
                                    jsonObjectInvoice.getInt("idgroup")

                            ));
                        }

                        //Una vez tenemos la respuesta y la lista la retornamos
                        webApiRequestJsonObjectArrayListener.onSuccess(jsonResponseModel, invoiceModel);
                    } else {
                        //Si la respuesta es negativa devolvemos el error
                        webApiRequestJsonObjectArrayListener.onError(jsonResponseModel);
                    }

                } catch (JSONException e) {
                    webApiRequestJsonObjectArrayListener.onError(new JsonResponseModel(-2, "getInvoiceByGroup JSONException: Error al generar el objeto JSON" + e.getMessage()));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "getInvoiceByGroup VolleyError: " + error.getMessage());
                webApiRequestJsonObjectArrayListener.onError(new JsonResponseModel(-1, "getInvoiceByGroup onErrorResponse: " + error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + idGroup);
                Map<String, String> params = new HashMap<String, String>();
                params.put("idgroup", String.valueOf(idGroup));

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
    public void isInvoiceByGroup(String idGroup, WebApiRequestJsonObjectListener webapirequestjsonobjectlistener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "isInvoiceByGroup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "isInvoiceByGroup onResponse: response " + response);

                //Respuesta
                try {
                    JSONObject json = new JSONObject(response);
                    id = json.getInt("id");
                    message = json.getString("message");

                } catch (JSONException e) {
                    webapirequestjsonobjectlistener.onError(-2, "isInvoiceByGroup JSONException: Error al generar el objeto JSON");
                }

                webapirequestjsonobjectlistener.onSuccess(id, message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "isInvoiceByGroup VolleyError: " + error.getMessage());
                webapirequestjsonobjectlistener.onError(-1, "isInvoiceByGroup vVolleyError: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + idGroup);
                Map<String, String> params = new HashMap<String, String>();
                params.put("idgroup", idGroup);

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

    public void insertInvoice(InvoiceModel im, WebApiRequestJsonObjectListener webapirequestjsonobjectlistener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        @SuppressWarnings("RedundantThrows") StringRequest sr = new StringRequest(Request.Method.POST, URL + "insertInvoice.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "insertInvoice onResponse: response " + response);

                //Respuesta
                try {
                    JSONObject json = new JSONObject(response);
                    id = json.getInt("id");
                    message = json.getString("message");
                } catch (JSONException e) {
                    webapirequestjsonobjectlistener.onError(-2, "insertInvoice JSONException: Error al generar el objeto JSON");
                }

                webapirequestjsonobjectlistener.onSuccess(id, message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "insertInvoice VolleyError: " + error.getMessage());
                webapirequestjsonobjectlistener.onError(-1, "insertInvoice vVolleyError: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Date date = new Date();
                // String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
                Log.d("DEBUGME", "getparams: " + "identificador: " + im.getIdentifier());
                Map<String, String> params = new HashMap<String, String>();
                params.put("identifier", im.getIdentifier());
                params.put("type", im.getType());
                params.put("date", im.getDate());
                params.put("start_period", im.getStart_period());
                params.put("end_period", im.getEnd_period());
                params.put("consumption", String.valueOf(im.getConsumption()));
                params.put("amount", String.valueOf(im.getAmount()));
                params.put("idgroup", String.valueOf(im.getIdgroup()));


                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);

    }

    public void getGroupAndShared(String email, String password, int idgroup, WebApiRequestJsonObjectArrayListenerV2 webApiRequestJsonObjectArrayListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "getGroupAndShared.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "getGroupAndShared onResponse: response " + response);

                //Respuesta
                try {
                    //Obtenemos el JsonResponde Padre
                    JSONObject json = new JSONObject(response);

                    //Obtenemos el JsonObject hijo con la respuesta
                    JSONObject jsonObjectResponse = json.getJSONObject("response");
                    JsonResponseModel jsonResponseModel = new JsonResponseModel(
                            jsonObjectResponse.getInt("id"),
                            jsonObjectResponse.getString("message"));

                    if (jsonResponseModel.getId() > 0) {
                    JSONObject jsonObjectGroup = json.getJSONObject("group");
                    //Creamos la lista de grupos
                   GroupModel group = new GroupModel(
                           jsonObjectGroup.getInt("idgroup"),
                           jsonObjectGroup.getString("name"),
                           jsonObjectGroup.getString("description"),
                           jsonObjectGroup.getString("currency"));



                        //Creamos la lista de shared
                        ArrayList<SharedModel> shared = new ArrayList<SharedModel>();

                        //Si la respuesta es Positiva hay datos. Obtenemos el JsonArray
                        JSONArray jsonArrayShareds = json.getJSONArray("shared");
                        for (int i = 0; i < jsonArrayShareds.length(); i++) {
                            //Obtenemos el objeto JSONObjet de shared individual
                            JSONObject jsonObjectShared = jsonArrayShareds.getJSONObject(i);
                            shared.add(new SharedModel(
                                    jsonObjectShared.getString("email"),
                                    jsonObjectShared.getInt("idgroup"),
                                    jsonObjectShared.getInt("role")
                            ));
                        }


                        //Una vez tenemos la respuesta y la lista la retornamos
                        webApiRequestJsonObjectArrayListener.onSuccess(jsonResponseModel, group, shared);
                    } else {
                        //Si la respuesta es negativa devolvemos el error
                        webApiRequestJsonObjectArrayListener.onError(jsonResponseModel);
                    }

                } catch (JSONException e) {
                    webApiRequestJsonObjectArrayListener.onError(new JsonResponseModel(-2, "getGroupAndShared JSONException: Error al generar el objeto JSON"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "getGroupAndShared VolleyError: " + error.getMessage());
                webApiRequestJsonObjectArrayListener.onError(new JsonResponseModel(-1, "getGroupAndShared onErrorResponse: " + error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + email + " " + password + " idGroup:" + idgroup);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("idgroup", idgroup + "");

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
    public void updateGroup(String email,
                            String password,
                            String groupname,
                            String groupdescription,
                            String groupcurrency,
                            ArrayList<MemberModel> membersAdd,
                            ArrayList<MemberModel> membersUpd,
                            ArrayList<MemberModel> membersDel,
                            WebApiRequestJsonResponseListener webapirequestjsonresponselistener) {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, URL + "updateGroup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUGME", "updateGroup onResponse: response " + response);

                //Respuesta
                try {
                    JSONObject json = new JSONObject(response);
                    id = json.getInt("id");
                    message = json.getString("message");
                    JsonResponseModel jsonResponseModel = new JsonResponseModel(id, message);
                    if (jsonResponseModel.getId() > 0) {
                        webapirequestjsonresponselistener.onSuccess(jsonResponseModel);
                    } else {
                        webapirequestjsonresponselistener.onError(jsonResponseModel);
                    }
                } catch (JSONException e) {
                    webapirequestjsonresponselistener.onError(new JsonResponseModel(-2, "updateGroup JSONException: Error al generar el objeto JSON"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUGME", "updateGroup VolleyError: " + error.getMessage());
                webapirequestjsonresponselistener.onError(new JsonResponseModel(-1, "updateGroup vVolleyError: " + error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("DEBUGME", "getparams: " + email);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("groupname", groupname);
                params.put("groupdescription", groupdescription);
                params.put("groupcurrency", groupcurrency);

                int a = 1;
                for (MemberModel m : membersAdd) {
                    String member = "membersAdd" + a;
                    String role = "roleAdd" + a;
                    Log.d("DEBUGME", m.getEmail() + " " + m.getRole() + " " + member + " " + role);
                    params.put(member, m.getEmail());
                    params.put(role, String.valueOf(m.getRole()));
                    a++;
                }

                int b = 1;
                for (MemberModel m : membersUpd) {
                    String member = "membersUpd" + b;
                    String role = "roleUpd" + b;
                    Log.d("DEBUGME", m.getEmail() + " " + m.getRole() + " " + member + " " + role);
                    params.put(member, m.getEmail());
                    params.put(role, String.valueOf(m.getRole()));
                    b++;
                }

                int c = 1;
                for (MemberModel m : membersDel) {
                    String member = "membersDel" + c;
                    Log.d("DEBUGME", m.getEmail() + " " + m.getRole() + " " + member );
                    params.put(member, m.getEmail());
                    c++;
                }
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
