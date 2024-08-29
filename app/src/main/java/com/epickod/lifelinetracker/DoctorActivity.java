package com.epickod.lifelinetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.epickod.lifelinetracker.databinding.ActivityDoctorBinding;

public class DoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        ActivityDoctorBinding binding = ActivityDoctorBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        binding.btnLogout.setOnClickListener(v -> {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }
}