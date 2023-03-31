package SkinConsultationCentre;

import guiComponents.myFrame;

import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static SkinConsultationCentre.WestminsterSkinConsultationManager.doctorArrayList;
import static SkinConsultationCentre.WestminsterSkinConsultationManager.patientList;

public class GUI extends JFrame {

    public void menu() {

        // Create a new JFrame to hold the buttons

        myFrame frame = new myFrame();

        frame.setLayout(new FlowLayout());

        // Create the buttons
        JButton addConsultationBtn = new JButton("Add Consultation");
        JButton viewDocBtn = new JButton("View Available Doctors");
        JButton pastConsultationsBtn = new JButton("View past Consultations");
        JButton exitBtn = new JButton("Exit");

        // Add the buttons to the frame
        frame.add(addConsultationBtn);
        frame.add(viewDocBtn);
        frame.add(pastConsultationsBtn);
        frame.add(exitBtn);

        // Set the title of the frame and make it visible
        frame.setTitle("Menu");
        frame.setVisible(true);

        // Add action listeners to the buttons that open the corresponding pages and close the current frame when clicked.
        addConsultationBtn.addActionListener(e -> {
            frame.dispose();
            consultationPage();
        });


        viewDocBtn.addActionListener(e -> {
            frame.dispose();
            viewAvailableDoctors();
        });

        pastConsultationsBtn.addActionListener(e -> {
            frame.dispose();
            viewPastConsultations();
        });

        exitBtn.addActionListener(e -> frame.dispose());
    }

    public void consultationPage() {

        // Set up the frame
        setTitle("Doctor List");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 5));

        //JCombo box to list the doctors
        JComboBox<String> doctorComboBox = new JComboBox<>();

        //JLabel comboLabel = new JLabel("Select a doctor: ");

        for (Doctor doctor : doctorArrayList) {
            doctorComboBox.addItem("Dr. " + doctor.getFName() + " " + doctor.getSurName() + " | " + doctor.getSpecialization());
        }

        JPanel selection = new JPanel();

        selection.add(doctorComboBox);

        add(selection, BorderLayout.CENTER);
        setVisible(true);

        // Create a panel for the date and time inputs
        JPanel inputPanel = new JPanel();

        // Add a label and a text field for the date
        JLabel dateLabel = new JLabel("Date (yyyy/mm/dd): ");
        JTextField dateField = new JTextField(10);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);

        // Add a label and a text field for the time
        JLabel startTimeLabel = new JLabel("Start Time (hh:mm): ");
        JTextField startTimeField = new JTextField(10);
        inputPanel.add(startTimeLabel);
        inputPanel.add(startTimeField);

        // Add a label and a text field for the time
        JLabel endTimeLabel = new JLabel("end Time (hh:mm): ");
        JTextField endTimeField = new JTextField(10);
        inputPanel.add(endTimeLabel);
        inputPanel.add(endTimeField);

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        inputPanel.add(submitButton);

        // Add the input panel to the frame
        add(inputPanel, BorderLayout.NORTH);

        // Create a pattern for the date format "mm/dd/yyyy"
        Pattern datePattern = Pattern.compile("^(\\d{4})/(0[1-9]|1[0-2])/(0[1-9]|[1-2]\\d|3[0-1])$");

        // Create a pattern for the time format "hh:mm"
        Pattern timePattern = Pattern.compile("^([0-1]\\d|2[0-3]):[0-5]\\d$");

        submitButton.addActionListener(e -> {
            // Get the user's input in the date and time text fields
            String dateInput = dateField.getText();
            String startTimeInput = startTimeField.getText();
            String endTimeInput = endTimeField.getText();

            // Set the background colors of the date and time text fields to the default
            dateField.setBackground(null);
            startTimeField.setBackground(null);

            // Create a matcher for the date input
            Matcher dateMatcher = datePattern.matcher(dateInput);
            // Create a matcher for the start time input
            Matcher startTimeMatcher = timePattern.matcher(startTimeInput);
            // Create a matcher for the end time input
            Matcher endTimeMatcher = timePattern.matcher(endTimeInput);

            // Check if both the date and time inputs are in the correct format
            if (dateMatcher.matches() && startTimeMatcher.matches() && endTimeMatcher.matches()) {
                // Concatenate the date input and time input to create a combined date-time string in the format "yyyy/MM/dd HH:mm"
                String startDateTime = dateInput + " " + startTimeInput;
                String endDateTimeInput = dateInput + " " + endTimeInput;

                // Convert the combined date-time input to a LocalDateTime object
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                LocalDateTime startTime = LocalDateTime.parse(startDateTime, formatter);
                LocalDateTime endTime = LocalDateTime.parse(endDateTimeInput, formatter);

                // Get the index of the selected doctor in the doctorArrayList
                int selectedIndex = doctorComboBox.getSelectedIndex();
                // Get the selected doctor object from the doctorArrayList
                Doctor selectedDoctor = doctorArrayList.get(selectedIndex);
                

                // Check if the selected doctor is available within the specified date and time period
                if (selectedDoctor.isAvailable(selectedDoctor, startTime, endTime)) {
                    JOptionPane.showMessageDialog(null, "Selected doctor is available within the specified date and time period");
                    // If the doctor is available, book the consultation
                    dispose();
                    GUIPatientDetailsPage patientDetailsPage = new GUIPatientDetailsPage();
                    patientDetailsPage.patientDetailsPage(startTime, selectedIndex, endTime);
                } else {
                    // If the doctor is not available, find a random available doctor from the doctorArrayList
                    ArrayList<Doctor> availableDoctors = new ArrayList<>();
                    for (Doctor doc : doctorArrayList) {
                        if (doc.isAvailable(doc, startTime, endTime)) {
                            availableDoctors.add(doc);
                            System.out.println(availableDoctors);
                        }
                    }
                    if (!availableDoctors.isEmpty()) {
                        // If there are available doctors, randomly select one of them
                        Random rand = new Random();
                        int index = rand.nextInt(availableDoctors.size());
                        JOptionPane.showMessageDialog(null, "The Doctor you selected is not available within the specified date and time period, but we have selected another doctor available within that time period. Please enter your details below.");
                        dispose();
                        GUIPatientDetailsPage patientDetailsPage = new GUIPatientDetailsPage();
                        patientDetailsPage.patientDetailsPage(startTime, index, endTime);

                    } else {
                        JOptionPane.showMessageDialog(null, "There are no available doctors in the selected time.");

                    }
                }
            }
        });
    }

    public void viewPastConsultations() {
        // Set up the frame
        setTitle("Doctor List");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 5));

        Consultation.loadData(patientList);

        // Create a formatter for the date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");


        // Create a table model to store the data for the table
        DefaultTableModel tableModel = new DefaultTableModel();

        // Set the columns of the table
        tableModel.setColumnIdentifiers(new String[]{
                "Patient ID",
                "Patient Name",
                "Doctor",
                "Start DateTime",
                "End DateTime",
                "Cost",
                "Notes"
        });

        // Add the data for each consultation to the table model
        for (Consultation consultation : Consultation.getConsultationList()) {
            String pID = consultation.getPatient().getPatientId();
            String pName = consultation.getPatient().getFName() + " " + consultation.getPatient().getSurName();
            String doctorName = "Dr." + consultation.getDoctor().getFName() + " " + consultation.getDoctor().getSurName();
            String startDateTime = consultation.getDateTime().format(formatter);
            String endDateTime = consultation.getEndDateTime().format(formatter);
            double cost = consultation.getCost();
            String notes = consultation.getNotes();

            tableModel.addRow(new Object[]{
                    pID,
                    pName,
                    doctorName,
                    startDateTime,
                    endDateTime,
                    cost,
                    notes
            });
        }
        // Create a panel for the input fields and the view notes button
        JPanel inputPanel = new JPanel();

        // Add a label and a text field for the date-time input
        JLabel dateTimeInputLabel = new JLabel("Date (yyyy/mm/dd hh:mm): ");
        JTextField dateTimeInputField = new JTextField(16);

        // Add a label and a text field for the patient ID input
        JLabel pIdLabel = new JLabel("Enter patient's ID : ");
        JTextField pIdField = new JTextField(15);

        // Add a button to view the notes for a consultation
        JButton viewNotesButton = new JButton("View Notes");

        // Add a label to display the notes for the selected consultation
        JLabel notesLabel = new JLabel("Img notes for selected consultation ");
        JLabel imagePreviewLabel = new JLabel("Img notes for selected consultation ");

        imagePreviewLabel.setPreferredSize(new Dimension(200, 200));
        imagePreviewLabel.setVerticalAlignment(JLabel.TOP);
        imagePreviewLabel.setHorizontalAlignment(JLabel.LEFT);
        imagePreviewLabel.setText("");

        // Adding buttons,labels and text fields to the panel
        inputPanel.add(dateTimeInputField);
        inputPanel.add(dateTimeInputLabel);
        inputPanel.add(pIdField);
        inputPanel.add(pIdLabel);
        inputPanel.add(viewNotesButton);
        inputPanel.add(notesLabel);
        inputPanel.add(imagePreviewLabel);

        // Action listener for view notes button
        viewNotesButton.addActionListener(event -> {

            String pIdInput = pIdField.getText();

            String input = dateTimeInputField.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                Date dateTime = sdf.parse(input);
                // Convert the Date object to an Instant
                Instant instant = dateTime.toInstant();
                // Create a LocalDateTime object from the Instant
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                DateTimeFormatter decFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
                EncryptDecrypt enc = new EncryptDecrypt();
                try {
                    enc.imgDecrypt(localDateTime, pIdInput);
                    String subfolder = "Encryption" + File.separator + "img";
                    String imagePath = localDateTime.format(decFormatter) + pIdInput + "-DecryptedImg.jpg";
                    ImageIcon imageIcon = new ImageIcon(subfolder + File.separator + imagePath);
                    Image image = imageIcon.getImage();
                    Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(scaledImage);
                    imagePreviewLabel.setIcon(imageIcon);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    EncryptDecrypt ed = new EncryptDecrypt();
                    String decrypted = ed.txtDecrypt(localDateTime, pIdInput);
                    notesLabel.setText(decrypted);

                } catch (NoSuchPaddingException | IOException | NoSuchAlgorithmException |
                         InvalidKeyException e) {
                    throw new RuntimeException(e);
                }

                add(inputPanel, BorderLayout.NORTH);

                // The input is a valid date and time, do something here
            } catch (ParseException e) {
                // The input is not a valid date and time, highlight the text field in red and show an error message
                dateTimeInputField.setBackground(Color.RED);
                JOptionPane.showMessageDialog(null, "Incorrect date and time format. Please enter the date and time in the format 'yyyy/mm/dd hh:mm'.");
            }

        });

        // Create a new table using the table model
        JTable table = new JTable(tableModel);

        // Add the table to a scroll pane so that it can be scrolled if the data doesn't fit on the screen
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel();

        JButton goBackButton = new JButton("Go Back");
        buttonPanel.add(goBackButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame to visible
        setVisible(true);

        goBackButton.addActionListener(event -> {

            // Close the current frame
            setVisible(false);
            dispose();

            // Open a new frame
            GUI guiImplementation = new GUI();
            guiImplementation.menu();
        });

        add(inputPanel, BorderLayout.NORTH);

        // Add the scroll pane to the frame
        add(scrollPane);
    }

    public void viewAvailableDoctors() {

        // Set up the frame
        setTitle("Doctor List");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 5));

        // Create a table to display the doctor data
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("SurName");
        tableModel.addColumn("Date of Birth");
        tableModel.addColumn("Contact No");
        tableModel.addColumn("Medical License");
        tableModel.addColumn("Specialization");
        JTable doctorTable = new JTable(tableModel);

        add(doctorTable);
        // Get the table header
        JTableHeader header = doctorTable.getTableHeader();

        // Add the header to the frame
        add(header, BorderLayout.NORTH);

        // Add the data for the doctors to the table
        for (Doctor doctor : doctorArrayList) {
            tableModel.addRow(new Object[]{
                    doctor.getFName(), doctor.getSurName(), doctor.getDob(), doctor.getContactNo(), doctor.getMedLicense(), doctor.getSpecialization()
            });
        }

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();

        // Add the "Go Back" button to the panel
        JButton goBackButton = new JButton("Go Back");
        buttonPanel.add(goBackButton);

        // Add the "Sort Alphabetically" button to the panel
        JButton sortByFButton = new JButton("Sort Alphabetically by first name");
        buttonPanel.add(sortByFButton);

        // Add the "Sort Alphabetically" button to the panel
        JButton sortByLButton = new JButton("Sort Alphabetically by SurName");
        buttonPanel.add(sortByLButton);

        // Add the panel to the bottom of the frame
        add(buttonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(event -> {

            // Close the current frame
            setVisible(false);
            dispose();

            // Open a new frame
            GUI guiImplementation = new GUI();
            guiImplementation.menu();
        });

        sortByFButton.addActionListener(event -> {

            // Clear the table
            tableModel.setRowCount(0);

            // Sort the list of doctors
            doctorArrayList.sort(Comparator.comparing(Person::getFName));

            // Add the sorted data to the table
            for (Doctor doctor : doctorArrayList) {
                tableModel.addRow(new Object[]{
                        doctor.getFName(), doctor.getSurName(), doctor.getDob(), doctor.getContactNo(), doctor.getMedLicense(), doctor.getSpecialization()
                });
            }
        });

        sortByLButton.addActionListener(event -> {

            // Clear the table
            tableModel.setRowCount(0);

            // Sort the list of doctors
            doctorArrayList.sort(Comparator.comparing(Person::getSurName));

            // Add the sorted data to the table
            for (Doctor doctor : doctorArrayList) {
                tableModel.addRow(new Object[]{
                        doctor.getFName(), doctor.getSurName(), doctor.getDob(), doctor.getContactNo(), doctor.getMedLicense(), doctor.getSpecialization()
                });
            }
        });

        // Show the frame
        setVisible(true);
    }

}