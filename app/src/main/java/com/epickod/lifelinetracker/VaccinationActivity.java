package com.epickod.lifelinetracker;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.epickod.lifelinetracker.classes.VaccinationAdapter;
import com.epickod.lifelinetracker.classes.VaccinationRecord;
import com.epickod.lifelinetracker.classes.VolleySingleton;
import com.epickod.lifelinetracker.databinding.ActivityVaccinationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class VaccinationActivity extends AppCompatActivity {


    private VaccinationAdapter adapter;
    private List<VaccinationRecord> vaccinationRecords = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVaccinationBinding binding = ActivityVaccinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VaccinationAdapter(vaccinationRecords);
        binding.recyclerview.setAdapter(adapter);


        binding.btnAddVaccinationRecord.setOnClickListener(v -> {
            Intent i = new Intent(VaccinationActivity.this, AddVaccinationActivity.class);
            startActivity(i);
        });



        initToolbar(binding);

        fetchVaccinationRecords();
    }

    private void initToolbar(ActivityVaccinationBinding binding) {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar());
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    private void fetchVaccinationRecords() {
        String url = api_url() + "get-vaccination-history?patient_id=" + Paper.book().read("user_id");

        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject record = response.getJSONObject(i);
                            String vaccineName = record.getString("vaccine_name");
                            String vaccineDose = record.getString("vaccine_dose");
                            String vaccineDate = record.getString("vaccine_date");
                            String vaccinationStatus = record.getString("vaccination_status");

                            vaccinationRecords.add(new VaccinationRecord(vaccineName, vaccineDose, vaccineDate, vaccinationStatus));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.d("TAG", "Error: " + error.getMessage()));

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }


}