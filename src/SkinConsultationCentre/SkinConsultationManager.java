package SkinConsultationCentre;

public interface SkinConsultationManager {
    void addDoctor(String fName, String surName, String dob, String contactNo, String medL, String special);

    void removeDuplicatesMedIds();

    void deleteDoctor(String medL);

    void sortDoctors();

    void showDoctors();

    void saveDoctors();

    void loadDoctors();

    void menu();

    boolean getDecision(Runnable method);

}