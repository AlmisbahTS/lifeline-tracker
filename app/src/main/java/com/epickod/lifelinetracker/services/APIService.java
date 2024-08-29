package com.epickod.lifelinetracker.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class APIService {

    private static RequestQueue requestQueue;

    private static void initRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static void getRequest(Context context, String url, final Response.Listener<String> listener, final Response.ErrorListener errorListener) {
        initRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        requestQueue.add(stringRequest);
    }

    public static void postRequest(Context context, String url, JSONObject postData, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        initRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }
}
