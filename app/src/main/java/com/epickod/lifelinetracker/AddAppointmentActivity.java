package com.epickod.lifelinetracker;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.epickod.lifelinetracker.classes.TimeSlotAdapter;
import com.epickod.lifelinetracker.classes.VolleySingleton;
import com.epickod.lifelinetracker.databinding.ActivityAddAppointmentBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.paperdb.Paper;

public class AddAppointmentActivity extends AppCompatActivity {

    private ActivityAddAppointmentBinding binding;
    private ArrayList<String> doctorList;
    private String selectedDoctor;
    private String selectedDate;
    private String selectedTimeSlot;
    private TimeSlotAdapter timeSlotAdapter;
    private List<String> timeSlots;
    private List<Boolean> availability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAppointmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        doctorList = new ArrayList<>();
        doctorList.add("Select Doctor");
        loadDoctors();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.datePicker.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
                selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                selectedDoctor = binding.doctorSpinner.getSelectedItem().toString();
                loadTimeSlots(selectedDoctor, selectedDate);

            });
        }

        binding.bookButton.setOnClickListener(v -> {
            selectedDoctor = binding.doctorSpinner.getSelectedItem().toString();
            int day = binding.datePicker.getDayOfMonth();
            int month = binding.datePicker.getMonth() + 1;
            int year = binding.datePicker.getYear();
            selectedDate = year + "-" + month + "-" + day;

            if (selectedTimeSlot != null) {
                bookAppointment(selectedDoctor, selectedDate, selectedTimeSlot);
            } else {
                Toast.makeText(AddAppointmentActivity.this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            }
        });


        initToolbar(binding);
    }
    private void initToolbar(  ActivityAddAppointmentBinding binding) {
        setSupportActionBar(this.binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar());
        this.binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    private void loadDoctors() {
        // Replace with your API URL to get the list of doctors
        String url = api_url() + "getDoctors";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String doctorName = jsonObject.getString("name");
                            doctorList.add(doctorName);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddAppointmentActivity.this,
                                android.R.layout.simple_spinner_item, doctorList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.doctorSpinner.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Volley", "Error: " + error.toString()));


        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void loadTimeSlots(String doctor, String date) {
        // Replace with your API URL to get the list of booked slots for the selected doctor and date
        String url = api_url() + "getBookedSlots?doctor=" + doctor + "&date=" + date;

        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        timeSlots = generateTimeSlots();
                        availability = new ArrayList<>(Collections.nCopies(timeSlots.size(), true));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String slot = jsonArray.getString(i);
                            Log.d("each slot", "loadTimeSlots: " + slot);
                            int index = timeSlots.indexOf(slot);
                            if (index >= 0) {
                                availability.set(index, false);
                            }
                        }

                        timeSlotAdapter = new TimeSlotAdapter(timeSlots, availability, timeSlot -> selectedTimeSlot = timeSlot);
                        binding.timeSlotsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                        binding.timeSlotsRecyclerView.setAdapter(timeSlotAdapter);
                        timeSlotAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.d("Errrorr", "loadTimeSlots: " + e.getMessage());
                        Toast.makeText(this, "Errrorr: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Volley", "Error: " + error.toString()));

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void bookAppointment(String doctor, String date, String timeSlot) {
        // Replace with your API URL to book the appointment
        String url = api_url() + "bookAppointment";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("bookAppointment", "bookAppointment: " + response);
                    Toast.makeText(AddAppointmentActivity.this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Log.e("Volleyxerror", "Error: " + error.toString());
                    Toast.makeText(AddAppointmentActivity.this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("doctor", doctor);
                params.put("date", date);
                params.put("time_slot", timeSlot);
                params.put("user_id", Paper.book().read("user_id"));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Paper.book().read("token"));
                return headers;
            }
        };

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private List<String> generateTimeSlots() {
        List<String> slots = new ArrayList<>();
        for (int i = 8; i <= 16; i += 2) {
            slots.add(i + ":00 - " + (i + 2) + ":00");
        }
        return slots;
    }
}
