package SkinConsultationCentre;

import guiComponents.myFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import java.time.LocalDateTime;


import static SkinConsultationCentre.WestminsterSkinConsultationManager.doctorArrayList;
import static SkinConsultationCentre.WestminsterSkinConsultationManager.patientList;

public class GUIPatientDetailsPage {

    public void patientDetailsPage(LocalDateTime startDateTime, int selectedIndex, LocalDateTime endDateTime) {
        // Create a new frame for the patient details page
        myFrame frame = new myFrame();

        // Set the title and layout of the frame
        frame.setTitle("Patient Details");
        frame.setLayout(new BorderLayout(10, 5));

        // Create a panel for the input fields
        JPanel inputPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        // Add a label and a text field for the first name
        JLabel pIDLabel = new JLabel("Patient ID (Unique*): ");
        JTextField pIDField = new JTextField(5);
        inputPanel.add(pIDLabel);
        inputPanel.add(pIDField);

        // Add a label and a text field for the first name
        JLabel fNameLabel = new JLabel("First Name: ");
        JTextField fNameField = new JTextField(10);
        inputPanel.add(fNameLabel);
        inputPanel.add(fNameField);

        // Add a label and a text field for the surname
        JLabel surNameLabel = new JLabel("Surname: ");
        JTextField surNameField = new JTextField(10);
        inputPanel.add(surNameLabel);
        inputPanel.add(surNameField);

        // Add a label and a text field for the date of birth
        JLabel dobLabel = new JLabel("Date of Birth (yyyy/mm/dd): ");
        JTextField dobField = new JTextField(10);
        inputPanel.add(dobLabel);
        inputPanel.add(dobField);

        // Add a label and a text field for the contact number
        JLabel contactNoLabel = new JLabel("Contact Number: ");
        JTextField contactNoField = new JTextField(10);
        inputPanel.add(contactNoLabel);
        inputPanel.add(contactNoField);

        // Add a label and a text field for the contact number
        JLabel notesLabel = new JLabel("Enter any notes: ");
        JTextArea notesArea = new JTextArea(5, 20);
        inputPanel.add(notesLabel);
        inputPanel.add(notesArea);

        JLabel fileChooserLabel = new JLabel("Upload images (if you have any): ");
        JFileChooser fileChooser = new JFileChooser();
        File projectRoot = new File(".");
        fileChooser.setCurrentDirectory(projectRoot);
        inputPanel.add(fileChooserLabel);

        JLabel imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(200, 200));
        imagePreviewLabel.setVerticalAlignment(JLabel.TOP);
        imagePreviewLabel.setHorizontalAlignment(JLabel.LEFT);
        imagePreviewLabel.setText("");
        inputPanel.add(imagePreviewLabel);

        // Create a submit button
        JButton uploadButton = new JButton("Upload");
        inputPanel.add(uploadButton);
        JLabel imageUrlLabel = new JLabel();
        inputPanel.add(imageUrlLabel);

        inputPanel.add(uploadButton);

        uploadButton.addActionListener(event -> {
            // Show the file chooser and get the user's selection
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                // User selected a file
                File selectedFile = fileChooser.getSelectedFile();
                // You can now use the selectedFile object to read the contents of the image file
                ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(scaledImage);
                imagePreviewLabel.setIcon(imageIcon);
            }
        });

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        inputPanel.add(submitButton);

        // Add the "Go Back" button to the panel
        JButton goBackButton = new JButton("Go Back");
        buttonPanel.add(goBackButton);

        // Add the input panel to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame to visible
        frame.setVisible(true);

        goBackButton.addActionListener(event -> {

            // Close the current frame
            frame.setVisible(false);
            frame.dispose();

            // Open a new frame
            GUI guiImplementation = new GUI();
            guiImplementation.menu();
        });

        // Add action listeners to the submit button
        submitButton.addActionListener(e -> {
            // If the date and time are in the correct format, convert them to LocalDateTime object

            String pId = pIDField.getText().trim();
            String fName = fNameField.getText().trim();
            String surName = surNameField.getText().trim();
            String dob = dobField.getText().trim();
            String contactNumber = contactNoField.getText().trim();
            String notes = notesArea.getText().trim();
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Check if any of the input fields are empty except notes and images
            if (pId.isEmpty() || fName.isEmpty() || surName.isEmpty() || dob.isEmpty() || contactNumber.isEmpty()) {
                // If any of the fields are empty, display an error message
                JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
            } else {

                // If all the fields are filled in, create a new Patient object
                Doctor selectedDoctor = doctorArrayList.get(selectedIndex);

                if (selectedDoctor.isAvailable(selectedDoctor, startDateTime, endDateTime)) {

                    boolean patientFound = false;
                    for (Consultation consultedPatient : Consultation.getConsultationList()) {
                        if (consultedPatient.getPatient().getPatientId().equals(pId)) {
                            selectedDoctor.bookConsultation(consultedPatient.getPatient(), LocalDateTime.from(startDateTime), endDateTime, 25, notes, imagePath, pId);
                            Consultation.saveData(patientList);
                            patientFound = true;
                            break;
                        }
                    }
                    if (!patientFound) {
                        Patient patient = new Patient(pId, fName, surName, dob, contactNumber);
                        patientList.add(patient);
                        selectedDoctor.bookConsultation(patient, LocalDateTime.from(startDateTime), LocalDateTime.from(endDateTime), 15, notes, imagePath, pId);
                        Consultation.saveData(patientList);

                    }

                }
                JOptionPane.showMessageDialog(null, "Consultation booked successfully! Please add any notes or upload images below.");

            }

        });

    }


}