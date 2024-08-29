package com.epickod.lifelinetracker;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.epickod.lifelinetracker.classes.MedicalHistory;
import com.epickod.lifelinetracker.classes.MedicalHistoryAdapter;
import com.epickod.lifelinetracker.classes.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class MedicalHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicalHistoryAdapter adapter;
    private List<MedicalHistory> medicalHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        initToolbar();

        (findViewById(R.id.btnAddRecord)).setOnClickListener(v -> {
            Intent intent = new Intent(MedicalHistoryActivity.this, AddMedicalHistoryActivity.class);
            startActivity(intent);
        });


        recyclerView = findViewById(R.id.recyclerViewMedicalHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicalHistoryList = new ArrayList<>();
        adapter = new MedicalHistoryAdapter(this, medicalHistoryList);
        recyclerView.setAdapter(adapter);


        fetchMedicalHistory();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private void fetchMedicalHistory() {
        String url = api_url() + "get-medical-history?patient_id=" + Paper.book().read("user_id");

        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Log.d("TAG", "fetchMedicalHistory: " + jsonObject.toString());

                            String date = jsonObject.getString("date");
                            String description = jsonObject.getString("description");
                            String illnessType = jsonObject.getString("illness_type");
                            String medicinePrescribed = jsonObject.getString("medicine_prescribed") + api_url().substring(0, api_url().length() - 4);
                            String doctorName = jsonObject.getString("doctor_name");
                            String prescriptionPhotoUrl = jsonObject.getString("prescription_photo");


                            MedicalHistory medicalHistory = new MedicalHistory(date, description, illnessType, medicinePrescribed, api_url().substring(0, api_url().length() - 4) + prescriptionPhotoUrl, doctorName);
                            medicalHistoryList.add(medicalHistory);

                        } catch (JSONException e) {
                            Log.d("TAG", "fetchMedicalHistory: " + e.getMessage());
                            Toast.makeText(MedicalHistoryActivity.this, "Failed to fetch data " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(MedicalHistoryActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }
}
