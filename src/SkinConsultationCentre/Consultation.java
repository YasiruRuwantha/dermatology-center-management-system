package SkinConsultationCentre;


import java.io.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


class Consultation {

    public static ArrayList<Consultation> getConsultationList() {
        return consultationList;
    }

    public static ArrayList<Consultation> consultationList = new ArrayList<>();

    private Patient patient;

    public Doctor getDoctor() {
        return doctor;
    }

    private Doctor doctor;
    private LocalDateTime dateTime;


    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }


    private LocalDateTime endDateTime;


    private double cost;
    private String notes;

    // Constructor
    // Constructor
    public Consultation(Patient patient, Doctor doctor, LocalDateTime dateTime, LocalDateTime endDateTime, double cost, String notes) {
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.cost = cost;
        this.notes = notes;
        this.endDateTime = endDateTime;
    }

    public static void addConsultationToList(Consultation consultation) {
        consultationList.add(consultation);
    }

    public static void savePatientList(List<Patient> patientList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Patient patient : patientList) {
                writer.write(patient.getPatientId() + "," + patient.getFName() + "," + patient.getSurName() + "," + patient.getDob() + "," + patient.getContactNo() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConsultationList(List<Consultation> consultationList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Consultation consultation : consultationList) {
                writer.write(consultation.getPatient().getPatientId() + "," + consultation.getPatient().getFName() + "," + consultation.getPatient().getSurName() + "," + consultation.getPatient().getDob() + "," + consultation.getPatient().getContactNo() + "," + consultation.getDoctor().getFName() + "," + consultation.getDoctor().getSurName() + "," + consultation.getDateTime() + "," + consultation.getEndDateTime() + "," + consultation.getCost() + "," + consultation.getNotes() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Patient> loadPatientList(String fileName) throws FileNotFoundException {
        List<Patient> patientList = new ArrayList<>();
        File file = new File(fileName);
        // Check if the file exists
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        // Read the data from the file and create a Patient object for each line

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Patient patient = new Patient(parts[0], parts[1], parts[2], parts[3], parts[4]);
                patientList.add(patient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return the list of Patient objects

        return patientList;
    }

    public static List<Consultation> loadConsultationList(String fileName) throws FileNotFoundException {
        List<Consultation> consultationList = new ArrayList<>();
        File file = new File(fileName);
        // If the file does not exist, a FileNotFoundException is thrown.
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        // Read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line on the comma character to get an array of values
                String[] parts = line.split(",");
                // Create a new Patient object from the first 5 values in the array
                Patient patient = new Patient(parts[0], parts[1], parts[2], parts[3], parts[4]);
                // Create a new Doctor object from the next 2 values in the array
                Doctor doctor = new Doctor(parts[5], parts[6], "", "", "", "");
                // Parse the next 2 values in the array into LocalDateTime objects
                LocalDateTime dateTime = LocalDateTime.parse(parts[7], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                LocalDateTime endDateTime = LocalDateTime.parse(parts[8], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

                double cost = Double.parseDouble(parts[9]);
                // Create a new Consultation object using the Patient, Doctor, LocalDateTime, and double values, as well as the final value in the array as the notes
                Consultation consultation = new Consultation(patient, doctor, dateTime, endDateTime, cost, parts[10]);
                // Add the consultation to the list
                consultationList.add(consultation);
            }
            // If there is an IOException, catch it and print the stack trace to the 
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return the list of consultations
        return consultationList;
    }

    public static void saveData(List<Patient> patientListArray) {

        Consultation.savePatientList(patientListArray, "patient_data.txt");

        Consultation.saveConsultationList(consultationList, "consultation_data.txt");

    }

    public static void loadData(List<Patient> patientListArray) {
        try {
            // The method first clears the patientListArray and consultationList
            patientListArray.clear();
            consultationList.clear();

            // Then, it loads the data from the patient_data.txt file into the patientListArray
            patientListArray.addAll(Consultation.loadPatientList("patient_data.txt"));
            consultationList.addAll(Consultation.loadConsultationList("consultation_data.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Files not found");
        }
    }


    @Override
    public String toString() {
        return "Consultation{" +
                "patient=" + patient +
                ", doctor=" + doctor +
                ", dateTime=" + dateTime +
                ", cost=" + cost +
                ", notes='" + notes + '\'' +
                '}';
    }

    // Getters and setters for the instance variables

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getCost() {
        return cost;
    }

    public String getNotes() {
        return notes;
    }

}