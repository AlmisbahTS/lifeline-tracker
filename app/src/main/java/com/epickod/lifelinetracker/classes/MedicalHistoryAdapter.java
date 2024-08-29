package com.epickod.lifelinetracker.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epickod.lifelinetracker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.MedicalHistoryViewHolder> {

    private Context context;
    private List<MedicalHistory> medicalHistoryList;

    public MedicalHistoryAdapter(Context context, List<MedicalHistory> medicalHistoryList) {
        this.context = context;
        this.medicalHistoryList = medicalHistoryList;
    }

    @NonNull
    @Override
    public MedicalHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medical_history, parent, false);
        return new MedicalHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalHistoryViewHolder holder, int position) {
        MedicalHistory medicalHistory = medicalHistoryList.get(position);

        holder.tvDate.setText(medicalHistory.getDate());
        holder.tvDescription.setText(medicalHistory.getDescription());
        holder.tvIllnessType.setText(medicalHistory.getIllnessType());
        holder.tvMedicinePrescribed.setText(medicalHistory.getMedicinePrescribed());
        holder.tvDoctorName.setText(medicalHistory.getDoctorName());

        Picasso.get().load(medicalHistory.getPrescriptionPhoto()).into(holder.imgPrescriptionPhoto);

    }

    @Override
    public int getItemCount() {
        return medicalHistoryList.size();
    }

    public static class MedicalHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDescription, tvIllnessType, tvMedicinePrescribed, tvDoctorName;
        ImageView imgPrescriptionPhoto;

        public MedicalHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvIllnessType = itemView.findViewById(R.id.tvIllnessType);
            tvMedicinePrescribed = itemView.findViewById(R.id.tvMedicinePrescribed);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            imgPrescriptionPhoto = itemView.findViewById(R.id.imgPrescriptionPhoto);
        }
    }
}
