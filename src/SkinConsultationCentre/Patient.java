package SkinConsultationCentre;

class Patient extends Person {
    private String patientId; // The ID of the patient

    public String getPatientId() {
        return patientId;
    }

    public Patient(String patientId, String fName, String surName, String dob, String contactNo) {
        super(fName, surName, dob, contactNo);
        this.patientId = patientId;
    }

}