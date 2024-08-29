package com.epickod.lifelinetracker;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.epickod.lifelinetracker.classes.Appointment;
import com.epickod.lifelinetracker.classes.AppointmentAdapter;
import com.epickod.lifelinetracker.classes.VolleySingleton;
import com.epickod.lifelinetracker.databinding.ActivityPatientDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.paperdb.Paper;

public class PatientDashboardActivity extends AppCompatActivity {
    private static final String TAG = "PatientDashboardActivity";
    private static final String URL = api_url() + "getUserInfo";
    private static String BEARER_TOKEN = null;
    ActivityPatientDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientDashboardBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        getUserInfo();


        BEARER_TOKEN = Paper.book().read("token");
        binding.btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        binding.btnAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAppointmentActivity.class);
            intent.putExtra("user_id", Objects.requireNonNull(Paper.book().read("user_id")).toString());
            startActivity(intent);
        });

        binding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, MedicalHistoryActivity.class);
            intent.putExtra("user_id", Objects.requireNonNull(Paper.book().read("user_id")).toString());
            startActivity(intent);
        });

        binding.btnVaccination.setOnClickListener(v -> {
            Intent intent = new Intent(this, VaccinationActivity.class);
            startActivity(intent);
        });

        fetchAppointments();


    }

    private void getUserInfo() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL + "/" + Paper.book().read("user_id"),
                null,
                response -> {
                    try {
                        JSONObject profile = response.getJSONObject("profile");
                        binding.phoneNumber.setText(profile.getString("phone"));
                        binding.email.setText(response.getString("email"));
                        binding.patientName.setText(response.getString("name"));
                        binding.gender.setText(profile.getString("gender"));
                        binding.address.setText(profile.getString("address"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d(TAG, "Response: " + response);
                },
                error -> {
                    // Handle the error
                    Log.e(TAG, "Error: " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + BEARER_TOKEN);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }



    private void fetchAppointments() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        String userId = Paper.book().read("user_id");
        String url = api_url() + "upcoming-appointments?user_id=" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Appointment> appointments = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Appointment appointment = new Appointment(
                                    jsonObject.getString("doctor_name"),
                                    jsonObject.getString("date"),
                                    jsonObject.getString("time_slot")
                            );
                            appointments.add(appointment);
                        }

                        AppointmentAdapter adapter = new AppointmentAdapter(appointments);
                        binding.recyclerview.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

}