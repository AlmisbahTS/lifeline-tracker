package com.epickod.lifelinetracker.classes;

public class MedicalHistory {
    private String date;
    private String description;
    private String illnessType;
    private String medicinePrescribed;
    private String prescriptionPhoto;
    private String doctorName;

    public MedicalHistory(String date, String description, String illnessType, String medicinePrescribed, String prescriptionPhoto, String doctorName) {
        this.date = date;
        this.description = description;
        this.illnessType = illnessType;
        this.medicinePrescribed = medicinePrescribed;
        this.prescriptionPhoto = prescriptionPhoto;
        this.doctorName = doctorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIllnessType() {
        return illnessType;
    }

    public void setIllnessType(String illnessType) {
        this.illnessType = illnessType;
    }

    public String getMedicinePrescribed() {
        return medicinePrescribed;
    }

    public void setMedicinePrescribed(String medicinePrescribed) {
        this.medicinePrescribed = medicinePrescribed;
    }

    public String getPrescriptionPhoto() {
        return prescriptionPhoto;
    }

    public void setPrescriptionPhoto(String prescriptionPhoto) {
        this.prescriptionPhoto = prescriptionPhoto;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
