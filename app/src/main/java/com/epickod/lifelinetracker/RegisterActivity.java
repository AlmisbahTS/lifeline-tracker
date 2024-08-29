package com.epickod.lifelinetracker;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, phoneEditText, usernameEditText, passwordEditText;
    private RadioGroup genderRadioGroup, roleRadioGroup;
    private TextView errorTextView;
    private Button registerButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        fullNameEditText = findViewById(R.id.fullNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        genderRadioGroup = findViewById(R.id.gender_radio);
        roleRadioGroup = findViewById(R.id.role_radio);
        registerButton = findViewById(R.id.loginButton);
        errorTextView = findViewById(R.id.errorTextView);


        requestQueue = Volley.newRequestQueue(this);


        registerButton.setOnClickListener(v -> registerUser());
    }

    @SuppressLint("SetTextI18n")
    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gender = selectedGenderButton != null ? selectedGenderButton.getText().toString().toLowerCase() : "";
        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRoleButton = findViewById(selectedRoleId);
        String role = selectedRoleButton != null ? selectedRoleButton.getText().toString().toLowerCase() : "";


        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty() || role.isEmpty()) {
            errorTextView.setText("All fields are required");
            errorTextView.setVisibility(View.VISIBLE);
            return;
        }

        // Create JSON object for request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", fullName);
            requestBody.put("phone", phone);
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("gender", gender);
            requestBody.put("role", role);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        StringRequest stringRequest = getStringRequest(requestBody);

        requestQueue.add(stringRequest);
    }

    @NonNull
    private StringRequest getStringRequest(JSONObject requestBody) {
        String url = api_url() + "register";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            errorTextView.setVisibility(View.GONE);
            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();

            

        }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                errorTextView.setText("Error: " + error.getMessage());
                errorTextView.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            public byte[] getBody() {
                return requestBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        return stringRequest;
    }
}
