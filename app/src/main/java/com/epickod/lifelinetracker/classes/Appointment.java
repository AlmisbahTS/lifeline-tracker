package com.epickod.lifelinetracker.classes;

public class Appointment {
    private String doctorName;
    private String date;
    private String timeSlot;

    public Appointment(String doctorName, String date, String timeSlot) {
        this.doctorName = doctorName;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
