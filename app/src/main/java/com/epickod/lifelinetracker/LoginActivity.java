package com.epickod.lifelinetracker;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.epickod.lifelinetracker.classes.CustomDialog;
import com.epickod.lifelinetracker.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private final String loginUrl = api_url() + "login";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Paper.init(this);
        setContentView(view);
        requestQueue = Volley.newRequestQueue(this);
        binding.loginButton.setOnClickListener(v -> {
            String username = Objects.requireNonNull(binding.usernameEditText.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.passwordEditText.getText()).toString().trim();

            // Perform login API call
            loginUser(username, password);
        });
    }

    private void loginUser(String username, String password) {

        CustomDialog.showCustomDialog(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.d("login-error", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginUrl, jsonObject, response -> {
            try {
                CustomDialog.dismissDialog(this);
                String token = response.getString("token");
                String role = response.getString("role");
                Paper.book().write("token", token);
                Paper.book().write("role", role);
                Paper.book().write("user_id", response.getString("user_id"));
                Intent i = new Intent(this, PatientDashboardActivity.class);
                startActivity(i);
                finish();
            } catch (JSONException e) {
                CustomDialog.dismissDialog(this);
                Log.d("login-error", e.toString());
            }
        }, error -> {
            Log.e("login-error", error.toString());
            Toast.makeText(this, "Error while logging in!" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            CustomDialog.dismissDialog(this);
        });

        requestQueue.add(jsonObjectRequest);
    }

}