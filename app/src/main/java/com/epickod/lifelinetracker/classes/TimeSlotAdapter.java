package com.epickod.lifelinetracker.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epickod.lifelinetracker.R;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<String> timeSlots;
    private List<Boolean> availability;
    private OnTimeSlotClickListener onTimeSlotClickListener;
    private int selectedPosition = -1; // To track the currently selected position

    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(String timeSlot);
    }

    public TimeSlotAdapter(List<String> timeSlots, List<Boolean> availability, OnTimeSlotClickListener onTimeSlotClickListener) {
        this.timeSlots = timeSlots;
        this.availability = availability;
        this.onTimeSlotClickListener = onTimeSlotClickListener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String timeSlot = timeSlots.get(position);
        boolean isAvailable = availability.get(position);

        holder.timeSlotButton.setText(timeSlot);
        holder.timeSlotButton.setEnabled(isAvailable);

        // Update the checked state based on the selected position
        holder.timeSlotButton.setChecked(position == selectedPosition);

        holder.timeSlotButton.setOnClickListener(v -> {
            // Update the selected position
            if (isAvailable) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged(); // Refresh the entire list to update the selection

                if (onTimeSlotClickListener != null) {
                    onTimeSlotClickListener.onTimeSlotClick(timeSlot);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        RadioButton timeSlotButton;

        public TimeSlotViewHolder(View itemView) {
            super(itemView);
            timeSlotButton = itemView.findViewById(R.id.timeSlotButton);
        }
    }
}
