package SkinConsultationCentre;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

import java.time.LocalDateTime;


import static SkinConsultationCentre.Consultation.consultationList;


class Doctor extends Person {
    private String medLicense;
    private String specialization;

    public Doctor(String fName, String surName, String dob, String contactNo, String medLicense, String specialization) {
        super(fName, surName, dob, contactNo);
        this.medLicense = medLicense;
        this.specialization = specialization;
    }


    public String getMedLicense() {
        return medLicense;
    }

    public void setMedLicense(String medLicense) {
        this.medLicense = medLicense;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }


    public boolean isAvailable(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {

        // Iterate through the list of consultations
        for (Consultation consultation : consultationList) {
            // Check if the consultation is with the given doctor
            if ((doctor.getFName() + doctor.getSurName()).equals(consultation.getDoctor().getFName() + consultation.getDoctor().getSurName())) {
                // Check if the consultation overlaps with the given time period
                if (((consultation.getDateTime()).isBefore(startTime) &&
                        (consultation.getEndDateTime()).isAfter(startTime)) ||
                        ((consultation.getDateTime()).isBefore(endTime) &&
                                (consultation.getEndDateTime()).isAfter(endTime)) ||
                        (consultation.getEndDateTime()).isEqual(startTime) ||
                        (consultation.getEndDateTime()).isEqual(endTime)) {
                    // The consultation overlaps with the given time period, return false
                    return false;
                }
            }
        }
        // No consultations were found to overlap with the given time period, means true
        return true;
    }


    public void bookConsultation(Patient patient, LocalDateTime dateTime, LocalDateTime endDateTime, int hourlyRate, String notes, String imagePath, String pId) throws RuntimeException {

        // Calculate the cost of the consultation based on the duration and hourly rate
        Duration duration = Duration.between(dateTime, endDateTime);
        double cost = (Integer.parseInt(String.valueOf(duration.toHours())) * hourlyRate);

        // If the notes are not null, encrypt the image file and save it
        if (notes != null) {
            EncryptDecrypt enc = new EncryptDecrypt();
            try {
                enc.imgEncrypt(imagePath, dateTime, pId);
                System.out.println("encrypted successfully");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Encrypt the notes for the consultation
        String encryptedNotes = "notes";

        // Encrypt the notes for the consultation
        try {
            EncryptDecrypt enc = new EncryptDecrypt();
            enc.txtEncrypt(notes, dateTime, pId);
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }


        // Create and return a new Consultation object
        Consultation bookedConsultation = new Consultation(patient, this, dateTime, endDateTime, cost, encryptedNotes);
        Consultation.addConsultationToList(bookedConsultation);


    }


}