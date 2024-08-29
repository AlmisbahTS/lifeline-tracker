package com.epickod.lifelinetracker;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.epickod.lifelinetracker.classes.VolleySingleton;
import com.epickod.lifelinetracker.databinding.ActivityAddVaccinationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.paperdb.Paper;

public class AddVaccinationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddVaccinationBinding binding = ActivityAddVaccinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initToolbar(binding);

        binding.btnSaveVaccine.setOnClickListener(v -> {
            saveVaccination(binding);
        });
    }

    private void initToolbar(ActivityAddVaccinationBinding binding) {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar());
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void saveVaccination(ActivityAddVaccinationBinding binding) {
        String vaccineName = binding.etVaccineName.getText().toString();
        String vaccineDose = binding.etVaccineDost.getText().toString();
        String vaccineDate = binding.etVaccineDate.getText().toString();
        int selectedStatusId = binding.radioGroupStatus.getCheckedRadioButtonId();
        RadioButton selectedStatusRadioButton = findViewById(selectedStatusId);
        String vaccinationStatus = selectedStatusRadioButton.getText().toString();

        if (vaccineName.isEmpty() || vaccineDose.isEmpty() || vaccineDate.isEmpty() || vaccinationStatus.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("patient_id", Paper.book().read("user_id")); // Assuming patient_id is stored in local storage
            postData.put("vaccine_name", vaccineName);
            postData.put("vaccine_dose", vaccineDose);
            postData.put("vaccine_date", vaccineDate);
            postData.put("vaccination_status", vaccinationStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = api_url() + "save-vaccination";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    Toast.makeText(AddVaccinationActivity.this, "Vaccination record added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }, error -> {
            Log.d("TAG", "saveVaccination: " + error.getLocalizedMessage());
            Toast.makeText(AddVaccinationActivity.this, "Failed to add vaccination record", Toast.LENGTH_SHORT).show();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}