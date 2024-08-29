package com.epickod.lifelinetracker.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epickod.lifelinetracker.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class VaccinationAdapter extends RecyclerView.Adapter<VaccinationAdapter.VaccinationViewHolder> {

    private List<VaccinationRecord> vaccinationList;

    // Constructor
    public VaccinationAdapter(List<VaccinationRecord> vaccinationList) {
        this.vaccinationList = vaccinationList;
    }

    @NonNull
    @Override
    public VaccinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_vaccination_record, parent, false);
        return new VaccinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccinationViewHolder holder, int position) {
        VaccinationRecord record = vaccinationList.get(position);
        holder.vaccineNameTextView.setText(record.getVaccineName());
        holder.vaccineDoseTextView.setText(record.getVaccineDose());
        holder.vaccineDateTextView.setText(record.getVaccineDate());
        holder.vaccinationStatusTextView.setText(record.getVaccinationStatus());
    }

    @Override
    public int getItemCount() {
        return vaccinationList.size();
    }

    static class VaccinationViewHolder extends RecyclerView.ViewHolder {

        TextView vaccineNameTextView;
        TextView vaccineDoseTextView;
        TextView vaccineDateTextView;
        Chip vaccinationStatusTextView;

        VaccinationViewHolder(View itemView) {
            super(itemView);
            vaccineNameTextView = itemView.findViewById(R.id.vaccineNameTextView);
            vaccineDoseTextView = itemView.findViewById(R.id.vaccineDoseTextView);
            vaccineDateTextView = itemView.findViewById(R.id.vaccineDateTextView);
            vaccinationStatusTextView = itemView.findViewById(R.id.vaccinationStatusTextView);
        }
    }
}
