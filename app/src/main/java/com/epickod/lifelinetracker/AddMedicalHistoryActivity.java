package com.epickod.lifelinetracker;

import static com.epickod.lifelinetracker.classes.Constents.api_url;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.epickod.lifelinetracker.classes.MultipartRequest;
import com.epickod.lifelinetracker.databinding.ActivityAddMedicalHistoryBinding;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class AddMedicalHistoryActivity extends AppCompatActivity {

    private ActivityAddMedicalHistoryBinding binding;
    private RequestQueue requestQueue;
    private Bitmap selectedImageBitmap;

    // Initialize the ActivityResultLauncher at the class level
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMedicalHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestQueue = Volley.newRequestQueue(this);

        // Register the launcher in onCreate
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().getData() != null) {
                            // Handle image selection from gallery
                            Uri selectedImageUri = result.getData().getData();
                            try {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                binding.ivPrescriptionPhoto.setImageBitmap(selectedImageBitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (result.getData() != null && result.getData().getExtras() != null) {
                            // Handle image capture from camera
                            selectedImageBitmap = (Bitmap) result.getData().getExtras().get("data");
                            binding.ivPrescriptionPhoto.setImageBitmap(selectedImageBitmap);
                        }
                    }
                });

        binding.btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedicalHistory();
            }
        });
    }

    private void showImagePickerDialog() {
        // Create a dialog to choose between camera and gallery
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooser = Intent.createChooser(galleryIntent, "Select Image");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        // Launch the intent using the previously registered launcher
        pickImageLauncher.launch(chooser);
    }

    private void saveMedicalHistory() {
        String date = binding.etDate.getText().toString();
        String description = binding.etDescription.getText().toString();
        String illnessType = binding.etIllnessType.getText().toString();
        String medicinePrescribed = binding.etMedicinePrescribed.getText().toString();
        String doctorName = binding.etDoctorName.getText().toString();

        if (date.isEmpty() || description.isEmpty() || illnessType.isEmpty() ||
                medicinePrescribed.isEmpty() || doctorName.isEmpty() || selectedImageBitmap == null) {
            Toast.makeText(this, "Please fill all fields and select a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert Bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        Map<String, String> params = new HashMap<>();
        params.put("date", date);
        params.put("patient_id", Paper.book().read("user_id"));
        params.put("description", description);
        params.put("illness_type", illnessType);
        params.put("medicine_prescribed", medicinePrescribed);
        params.put("doctor_name", doctorName);

        String url = api_url() + "save-medical-history";

        MultipartRequest multipartRequest = new MultipartRequest(url, params, imageBytes, "prescription_photo.jpg",
                response -> {
                    Toast.makeText(AddMedicalHistoryActivity.this, "Medical history added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Log.e("Volley Error", "Error saving data: " + error.fillInStackTrace());
                    Toast.makeText(AddMedicalHistoryActivity.this, "Failed to save medical history", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the Volley queue
        requestQueue.add(multipartRequest);
    }



}
