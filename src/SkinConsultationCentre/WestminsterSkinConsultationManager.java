package SkinConsultationCentre;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// This class represents and implements the SkinConsultationManager interface
public class WestminsterSkinConsultationManager implements SkinConsultationManager {
    // ArrayList to store the doctors at the consultation center
    static ArrayList<Doctor> doctorArrayList = new ArrayList<>();
    static List<Patient> patientList = new ArrayList<>();
    // private ArrayList<Consultation> consultationArrayList = new ArrayList<>();

    static int maxDoctors = 10;

    public static void main(String[] args) {
        WestminsterSkinConsultationManager doctorManager = new WestminsterSkinConsultationManager();

        doctorManager.loadDoctors(); // loading previously saved data.
        doctorManager.menu();

    }

    //User Menu to select options and get user inputs.
    @Override
    public void menu() {
        // Create a scanner to read user input
        Scanner sc = new Scanner(System.in);

        System.out.printf("%nWelcome to the %s%n%n", "Doctor Database System");
        // Set up a loop to keep the menu running
        boolean running = true;
        while (running) {
            System.out.printf("Please select an option:%n%n");

            System.out.printf("1. %s%n", "Add Doctor");
            System.out.printf("2. %s%n", "Delete Doctor");
            System.out.printf("3. %s%n", "Print Doctor List");
            System.out.printf("4. %s%n", "Save Information");
            System.out.printf("5. %s%n%n", "Open GUI");

            // Create a date format for parsing user input
            DateFormat df = new SimpleDateFormat("yyyy/M/d");
            df.getCalendar().setLenient(false);

            System.out.println("6. Exit");
            Scanner scanner = new Scanner(System.in);
            int selection = scanner.nextInt();
            switch (selection) {
                case 1 -> {
                    // Add a doctor
                    while (getDecision(() -> {
                        // Prompt the user for the doctor's details
                        System.out.println("Enter First Name: ");
                        String fName = sc.nextLine();
                        System.out.println("Enter Last Name: ");
                        String surName = sc.nextLine();
                        // Validate Date of birth input
                        Date tempDob = null;
                        String dob;
                        while (tempDob == null) {
                            System.out.print("Enter Date of Birth (YYYY/MM/DD): ");
                            try {
                                tempDob = df.parse(sc.nextLine());
                            } catch (ParseException e) {
                                System.out.println("Please enter a valid Date (YYYY/MM/DD)");
                            }
                        }
                        dob = df.format(tempDob);

                        // Validate contactNo input
                        int contactNo = 0;
                        while (contactNo == 0) {
                            System.out.println("Enter Contact No: ");
                            try {
                                contactNo = sc.nextInt();
                                // check that the contact number is a valid length
                                if (String.valueOf(contactNo).length() != 9) {
                                    System.out.println("Please enter a valid contact number (10 digits)");
                                    contactNo = 0;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid contact number (10 digits)");
                                sc.nextLine(); // discard invalid input
                            }
                        }
                        // Validate MedL input
                        int medL = 0;
                        while (medL == 0) {
                            System.out.println("Enter Medical license No: ");
                            try {
                                medL = sc.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid medical license number (integer)");
                                sc.nextLine(); // discard invalid input
                            }
                        }
                        sc.nextLine(); //skipping the nextLine
                        System.out.println("Enter Specialisation: ");
                        String special = sc.nextLine();

                        // Create a new Doctor object using the user's input
                        addDoctor(fName, surName, dob, String.valueOf(contactNo), String.valueOf(medL), special);
                        System.out.println("--Doctor Added Successfully--");
                        showDoctors();

                        System.out.println("Do you want to add a doctor again? (Y/N)");

                    })) {
                        System.out.println(" ");
                    }
                }
                case 2 -> {
                    // Delete a doctor
                    while (getDecision(() -> {

                        System.out.println("Available Doctors");
                        showDoctors(); // showing all the doctors.
                        System.out.println("\nselect the medical license no of the doctor : ");
                        String medL = sc.nextLine();
                        deleteDoctor(medL); // Delete the doctor with the specified medical license number.
                    })) {
                        System.out.println(" ");
                    }
                }
                case 3 ->
                    // Print the list of doctors
                        sortDoctors();
                case 4 ->
                    // Save the information to a file
                        saveDoctors();
                case 5 -> {
                    GUI gui = new GUI();
                    gui.menu();
                }
                case 6 ->
                    // Exit the program
                        running = false;
                default ->
                    // Invalid selection
                        System.out.println("Invalid selection. Please try again.");
            }
        }

    }

    @Override
    public void addDoctor(String fName, String surName, String dob, String contactNo, String medL, String special) {
        // Creating a new Doctor object with given details
        Doctor doctor = new Doctor(fName, surName, dob, contactNo, medL, special);

        // Check if the number of doctors at the consultation center is less than the maximum allowed.
        if (doctorArrayList.size() < maxDoctors) {

            // Set  values for the Doctor object
            doctor.setFName(fName);
            doctor.setSurName(surName);
            doctor.setDob(dob);
            doctor.setContactNo(contactNo);
            doctor.setMedLicense(medL);
            doctor.setSpecialization(special);

            // Adding Doctor object to the ArrayList of doctors
            doctorArrayList.add(doctor);

            // Call the method to remove any duplicate medical license numbers
            removeDuplicatesMedIds();

        } else {

            System.out.println("No more doctors can be added. Maximum number of doctors has been reached.");
            menu();

        }
    }

    // Method to remove any duplicate medical license numbers from the ArrayList of doctors
    @Override
    public void removeDuplicatesMedIds() {
        // Create a Set to store medical license numbers
        Set<String> medIds = new LinkedHashSet<>();

        // Iterate over the ArrayList of doctors
        for (Iterator<Doctor> record = doctorArrayList.iterator(); record.hasNext(); ) {
            // If the Set already contains the medical license number, remove the Doctor object from the ArrayList.
            if (!medIds.add(record.next().getMedLicense())) {
                record.remove();
                System.out.println("This Medical License is already registered to the system. Please try again.");

            }
        }
    }

    // Method to delete a doctor from the consultation center
    @Override
    public void deleteDoctor(String medL) {
        // Boolean flag to check if the needed doctor was found in the ArrayList
        boolean check = false;

        // Iterate through the list in reverse order
        for (int delNum = doctorArrayList.size() - 1; delNum >= 0; delNum--) {
            Doctor doctor = doctorArrayList.get(delNum);
            if (doctor.getMedLicense().equals(medL)) {
                check = true;
                System.out.println("-------------------------------Doctor deleted successfully.-------------------------------");
                System.out.printf("| %-6s | %-10s | %-10s | %-11s | %-10s | %-25s %n", "MedLNo", "FirstName", "LastName", "DOB", "ContactNo", "Specialization");
                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------");
                System.out.format("| %-6s | %-10s | %-10s | %-11s | %-10s | %-25s ", doctor.getMedLicense(), doctor.getFName(), doctor.getSurName(), doctor.getDob(), doctor.getContactNo(), doctor.getSpecialization());
                System.out.println(" ");
                System.out.println("------------------------------------------------------------------------------------------");
                System.out.println("------------------------------------------------------------------------------------------");

                // Remove the doctor from the list
                doctorArrayList.remove(delNum);

                System.out.println("Remaining Doctors in the center are " + doctorArrayList.size());
                break;
            }
        }

        if (!check) {
            System.out.println("Doctor not found");
            menu();
        }
    }

    @Override
    public void sortDoctors() {
        //Sorting the doctors in the doctor array list by surnames.
        doctorArrayList.sort(Comparator.comparing(Doctor::getSurName));
        showDoctors();

    }

    @Override
    public void showDoctors() {

        // Print a header for the list of doctors table
        System.out.printf("-----------------------------------------------------------------------------------------%n");
        System.out.printf("                             Skin Consultation Center         %n");
        System.out.printf("                             (List of Registered Doctors)         %n");

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("| %-6s | %-10s | %-10s | %-11s | %-10s | %-25s %n", "MedLNo", "FirstName", "LastName", "DOB", "ContactNo", "Specialization");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------");
        // Iterate over the ArrayList of doctors
        for (Doctor doctor : doctorArrayList) {
            {
                // Print the details for each doctor
                System.out.format("| %-6s | %-10s | %-10s | %-11s | %-10s | %-25s ", doctor.getMedLicense(), doctor.getFName(), doctor.getSurName(), doctor.getDob(), doctor.getContactNo(), doctor.getSpecialization());
                System.out.println();
            }
            System.out.println("-----------------------------------------------------------------------------------------");
        }

    }

    // Method to write the data for the doctors to a file
    @Override
    public void saveDoctors() {
        // Create a specific Scanner to read from the file
        File file = new File("doctorInfo.txt");

        try {
            // Create a file writer
            FileWriter info = new FileWriter(file, false);
            for (Doctor doctor : doctorArrayList) {

                // Write the data of each doctor to the file
                info.write(doctor.getFName() + " " +
                        doctor.getSurName() + " " +
                        doctor.getDob() + " " +
                        doctor.getContactNo() + " " +
                        doctor.getMedLicense() + " " +
                        doctor.getSpecialization() +
                        "\r");
            }
            System.out.println("Information has been stored successfully.");
            // Close the file writer
            info.close();

        } catch (IOException e) {
            System.out.println("File is unable to create");
        }
    }

    // Method to read  data for the doctors from a file
    @Override
    public void loadDoctors() {
        try {
            // Creating a txt file
            File file = new File("doctorInfo.txt");
            // Create a specific Scanner to read from the file
            Scanner scanDoctors = new Scanner(new File("doctorInfo.txt"));
            System.out.println("File " + file.getAbsolutePath());

            // Reading data of txt file while it has lines and storing them.
            while (scanDoctors.hasNextLine()) {

                String readLine = scanDoctors.nextLine();

                String fName = (readLine.split(" ")[0]);
                String surName = (readLine.split(" ")[1]);
                String date = (readLine.split(" ")[2]);
                String contact = (readLine.split(" ")[3]);
                String medL = (readLine.split(" ")[4]);
                String specialization = (readLine.split(" ")[5]);

                Doctor doctor = new Doctor(fName, surName, date, contact, medL, specialization);

                // Adding the Doctor object to the ArrayList of doctors
                doctorArrayList.add(doctor);
            }
            // Close the Scanner
            scanDoctors.close();

            System.out.println("Previous save file has been loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");

        }
    }

    // A method to prompt the user for a yes or no answer
    @Override
    public boolean getDecision(Runnable method) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            // Running the method passed as a parameter
            method.run();
            System.out.println("Enter Y to perform again or N to stop: ");
            String choose = sc.nextLine().toUpperCase();
            if (choose.equals("Y")) {
                return true;
            } else if (choose.equals("N")) {
                return false;
            } else {
                System.out.println("Please enter a valid input (Y or N)");
            }
        }
    }

    
}