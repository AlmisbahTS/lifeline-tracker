package com.epickod.lifelinetracker.classes;

public class VaccinationRecord {
    private String vaccineName;
    private String vaccineDose;
    private String vaccineDate;
    private String vaccinationStatus;

    // Constructor
    public VaccinationRecord(String vaccineName, String vaccineDose, String vaccineDate, String vaccinationStatus) {
        this.vaccineName = vaccineName;
        this.vaccineDose = vaccineDose;
        this.vaccineDate = vaccineDate;
        this.vaccinationStatus = vaccinationStatus;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getVaccineDose() {
        return vaccineDose;
    }

    public void setVaccineDose(String vaccineDose) {
        this.vaccineDose = vaccineDose;
    }

    public String getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(String vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    public String getVaccinationStatus() {
        return vaccinationStatus;
    }

    public void setVaccinationStatus(String vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }
}
