package com.epickod.lifelinetracker.services;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountAPIService {

    private Context context;

    public AccountAPIService(Context context) {
        this.context = context;
    }

    public void registerUser(String name, String email, String password, String role, String phone, String gender) {
        String url = api_url() + "register";

        // Creating a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Creating a JSON object to hold the request parameters
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("role", role);
            jsonBody.put("phone", phone);
            jsonBody.put("gender", gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating a new StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
                        System.out.println("Response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.err.println("Error: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }
}
